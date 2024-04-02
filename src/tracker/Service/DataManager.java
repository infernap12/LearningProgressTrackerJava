package tracker.Service;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Stats.CourseStatSummary;
import tracker.Model.Stats.PlatformStat;
import tracker.Model.Stats.PlatformStatSummary;
import tracker.Model.Submission;
import tracker.Service.DAO.*;

import java.util.*;

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

        Course[] mostPopular = getCourseStatArray(list, PlatformStat.StatType.POPULAR, true);
        System.out.println(Arrays.toString(mostPopular));
        Course[] leastPopular = getCourseStatArray(list, PlatformStat.StatType.POPULAR, false);
        //Activity

        Course[] highestActivity = getCourseStatArray(list, PlatformStat.StatType.ACTIVITY, true);
        Course[] lowestActivity = getCourseStatArray(list, PlatformStat.StatType.ACTIVITY, false);

        Course[] easiest = getCourseStatArray(list, PlatformStat.StatType.DIFFICULTY, true);
        Course[] hardest = getCourseStatArray(list, PlatformStat.StatType.DIFFICULTY, false);

        return new PlatformStatSummary(mostPopular, leastPopular, highestActivity, lowestActivity, easiest, hardest);
    }


    private Course[] getCourseStatArray(List<PlatformStat> platformStatList, PlatformStat.StatType statType, boolean most) {
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
                .toArray(Course[]::new);


    }

    public ISubmissionDao getSubmissionDao() {
        return submissionDao;
    }

    public IUserDao getUserDao() {
        return userDao;
    }

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
        //what's the point? we're just boxing a list? for a print method?

        return new CourseStatSummary(list);
    }
}
