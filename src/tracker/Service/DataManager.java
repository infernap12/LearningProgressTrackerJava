package tracker.Service;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Stats.CourseStatSummary;
import tracker.Model.Stats.PlatformStat;
import tracker.Model.Stats.PlatformStatSummary;
import tracker.Model.Submission;
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
        List<PlatformStat> list = submissionDao.getPlatformStats(courseDao);
        //look at IntSummaryStatistics, and consider copying it
        //Popularity

        Course[] mostPopular = getCourseStatArray(list, PlatformStat.StatType.POPULAR, true);
        Course[] leastPopular = getCourseStatArray(list, PlatformStat.StatType.POPULAR, false);
        //Activity

        Course[] highestActivity = getCourseStatArray(list, PlatformStat.StatType.ACTIVITY, true);
        Course[] lowestActivity = getCourseStatArray(list, PlatformStat.StatType.ACTIVITY, false);

        Course[] easiest = getCourseStatArray(list, PlatformStat.StatType.DIFFICULTY, true);
        Course[] hardest = getCourseStatArray(list, PlatformStat.StatType.DIFFICULTY, false);

        return new PlatformStatSummary(mostPopular, leastPopular, highestActivity, lowestActivity, easiest, hardest);
    }

//    private List<CourseStat> getCourseStats() {
//        List<CourseStat> list = new ArrayList<>();
//        Map<Course, List<Submission>> map = submissionDao.getAll().stream().collect(Collectors.groupingBy(x -> courseDao.get(x.courseID())));
//        for (Map.Entry<Course, List<Submission>> courseSubmissionPair : map.entrySet()) {
//            Course course = courseSubmissionPair.getKey();
//            int count = 0;
//            int pointTotal = 0;
//            int enrollments;
//            int avgPoints;
//            Set<Integer> userSet = new HashSet<>();
//            for (Submission submission : courseSubmissionPair.getValue()) {
//                count++;
//                userSet.add(submission.userID());
//                pointTotal += submission.points();
//            }
//            avgPoints = pointTotal / count;
//            enrollments = userSet.size();
//            list.add(new CourseStat(course, avgPoints, count, enrollments));
//        }
//        return list;
//    }

    private Map<Course, Integer> getDifficulties() {
        return submissionDao.getAll().stream()
                // Assuming courseID() returns an instance of Course.
                .collect(Collectors.groupingBy(submission -> courseDao.get(submission.courseID())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (int) entry.getValue().stream().mapToInt(Submission::points).average().orElseThrow())
                );
    }


    private Course[] getCourseStatArray(List<PlatformStat> statList, PlatformStat.StatType statType, boolean most) {
        IntSummaryStatistics minMax = statList.stream().mapToInt(x -> switch (statType) {
            case POPULAR -> x.enrollments();
            case ACTIVITY -> x.submissionCount();
            case DIFFICULTY -> (int) x.avgPoints();
        }).summaryStatistics();
        int value = most ? minMax.getMax() : minMax.getMin();

        return statList.stream().filter(platformStat -> switch (statType) {
            case POPULAR -> platformStat.enrollments() == value;
            case ACTIVITY -> platformStat.submissionCount() == value;
            case DIFFICULTY -> platformStat.avgPoints() == value;
        }).map(PlatformStat::course).toArray(Course[]::new);


    }

//    private Course[] getCourseStatArray(Map<Course, Integer> submissions, boolean most) {
//        IntSummaryStatistics stats = submissions.values().stream().mapToInt(Integer::intValue).summaryStatistics();
//        int value = most ? stats.getMax() : stats.getMin();
//
//        return submissions.entrySet().stream()
//                .filter(e -> e.getValue() == value)
//                .map(Map.Entry::getKey)
//                .toArray(Course[]::new);
//    }

    private Map<Course, Integer> getActivities() {
        return submissionDao.getAll().stream()
                // Assuming courseID() returns an instance of Course.
                .collect(Collectors.groupingBy(x -> courseDao.get(x.courseID())))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // This should not cause issues as it's a standard operation on Map.Entry
                        entry -> entry.getValue()
                                .size() // Counts the number of unique userIDs
                ));
    }
    private Map<Course, Integer> getEnrollments() {
    return submissionDao.getAll().stream()
            // Assuming courseID() returns an instance of Course.
            .collect(Collectors.groupingBy(x -> courseDao.get(x.courseID())))
            .entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey, // This should not cause issues as it's a standard operation on Map.Entry
                    entry -> entry.getValue().stream()
                            .map(Submission::userID) // Assuming userID() returns a unique identifier for a user
                            .collect(Collectors.toSet()) // This collects unique userIDs
                            .size() // Counts the number of unique userIDs
            ));
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
            submissionDao.add(new Submission(-1, id, i, point));
        }

    }

    public CourseStatSummary getCourseStats(Course course) {
        List<CourseStat> list = submissionDao.getCourseStats(course);

        return new CourseStatSummary(list);
    }
}
