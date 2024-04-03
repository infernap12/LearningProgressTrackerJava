package tracker.Service;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Stats.CourseStatSummary;
import tracker.Model.Stats.PlatformStat;
import tracker.Model.Stats.PlatformStatSummary;
import tracker.Model.Submission;
import tracker.Model.User;
import tracker.Service.DAO.*;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("DataFlowIssue")
//todo remove annotation by adding sqlite implementation
public class DataManager {

    private final ISubmissionDao submissionDao;
    private final IUserDao userDao;
    private final ICourseDao courseDao;

    public DataManager(boolean isInMemory) {
        if (isInMemory) {
            this.userDao = new HashMapUserDao();
            this.submissionDao = new HashMapSubmissionDao();
            this.courseDao = new HashMapCourseDao();
        } else {
            //todo sqlite implementation
            submissionDao = null;
            userDao = null;
            courseDao = null;
            //init DB and assign DAOs if sqlite
        }
    }

    public PlatformStatSummary getPlatformStats() {
        List<PlatformStat> list = submissionDao.getPlatformStats();
        //look at IntSummaryStatistics, and consider copying it
        //Popularity

        List<Course> mostPopular = getPlatformStatList(list, PlatformStat.StatType.POPULAR, true); //refactor to use lists
        // least popular as list, .removeAll(mostPopular)
        List<Course> leastPopular = getPlatformStatList(list, PlatformStat.StatType.POPULAR, false);
        leastPopular.removeAll(mostPopular);

        List<Course> highestActivity = getPlatformStatList(list, PlatformStat.StatType.ACTIVITY, true);
        List<Course> lowestActivity = getPlatformStatList(list, PlatformStat.StatType.ACTIVITY, false);
        lowestActivity.removeAll(highestActivity);

        List<Course> easiest = getPlatformStatList(list, PlatformStat.StatType.DIFFICULTY, true);
        List<Course> hardest = getPlatformStatList(list, PlatformStat.StatType.DIFFICULTY, false);
        hardest.removeAll(easiest);
        return new PlatformStatSummary(mostPopular, leastPopular, highestActivity, lowestActivity, easiest, hardest);
    }


    private List<Course> getPlatformStatList(List<PlatformStat> platformStatList, PlatformStat.StatType statType, boolean most) {
        IntSummaryStatistics minMax = platformStatList.stream()
                .mapToInt(x -> switch (statType) {
                    case POPULAR -> x.enrollments();
                    case ACTIVITY -> x.submissionCount();
                    case DIFFICULTY -> (int) x.avgPoints();
                })
                .summaryStatistics();
        int value = most ? minMax.getMax() : minMax.getMin();

        return platformStatList.stream()
                .filter(platformStat -> switch (statType) {
                    case POPULAR -> platformStat.enrollments() == value;
                    case ACTIVITY -> platformStat.submissionCount() == value;
                    case DIFFICULTY -> platformStat.avgPoints() == value;
                })
                .map(PlatformStat::courseID)
                .map(courseDao::get)
                .collect(Collectors.toCollection(ArrayList::new));


    }

    public ISubmissionDao getSubmissionDao() {
        return submissionDao;
    }

    public IUserDao getUserDao() {
        return userDao;
    }

    @SuppressWarnings("unused")
    public ICourseDao getCourseDao() {
        return courseDao;
    }


    public void addPoints(int id, int[] points) {
        for (int i = 0; i < points.length; i++) {
            int point = points[i];
            if (point == 0) {
                continue;
            }
            submissionDao.add(new Submission(-1, id, i, point));
        }

    }

    public CourseStatSummary getCourseStats(Course course) {
        List<CourseStat> list = submissionDao.getCourseStats(course);
        list.sort(CourseStat::compareTo);
        //what's the point? we're just boxing a list? for a print method?

        return new CourseStatSummary(list);
    }

    public Map<User, List<Course>> getNotifiableUsers() {
        List<User> users = userDao.getAll();
        Map<User, List<Course>> notifiableUsers = new HashMap<>();
        for (User user : users) {
            List<Course> courses = courseDao.getAll();
            courses.removeAll(user.getCompletedAndNotified());
            List<Course> maxPointsCourses = new ArrayList<>();
            for (Course course : courses) {
                int points = submissionDao.getPoints(user, course);
                if (points >= course.MAX_POINTS) {
                    maxPointsCourses.add(course);
                }
            }
            if (!maxPointsCourses.isEmpty()) {
                notifiableUsers.put(user, maxPointsCourses);
            }
        }

        //need to return a map of Users, with lists of courses for which they have hit max points, that aren't already notified.
        return notifiableUsers;
    }
}
