package tracker.Service;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Stats.PlatformStats;
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

    public PlatformStats getPlatformStats() {
        List<CourseStat> list = submissionDao.getCourseStats(courseDao);
        System.out.println(list);
        //look at IntSummaryStatistics, and consider copying it
        //Popularity

        Course[] mostPopular = getCourseStatArray(list, CourseStat.StatType.POPULAR, true);
        Course[] leastPopular = getCourseStatArray(list, CourseStat.StatType.POPULAR, false);
        //Activity

        Course[] highestActivity = getCourseStatArray(list, CourseStat.StatType.ACTIVITY, true);
        Course[] lowestActivity = getCourseStatArray(list, CourseStat.StatType.ACTIVITY, false);

        Course[] easiest = getCourseStatArray(list, CourseStat.StatType.DIFFICULTY, true);
        Course[] hardest = getCourseStatArray(list, CourseStat.StatType.DIFFICULTY, false);

        return new PlatformStats(mostPopular, leastPopular, highestActivity, lowestActivity, easiest, hardest);
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


    private Course[] getCourseStatArray(List<CourseStat> statList, CourseStat.StatType statType, boolean most) {
        IntSummaryStatistics minMax = statList.stream().mapToInt(x -> switch (statType) {
            case POPULAR -> x.enrollments();
            case ACTIVITY -> x.submissionCount();
            case DIFFICULTY -> (int) x.avgPoints();
        }).summaryStatistics();
        int value = most ? minMax.getMax() : minMax.getMin();

        return statList.stream().filter(courseStat -> switch (statType) {
            case POPULAR -> courseStat.enrollments() == value;
            case ACTIVITY -> courseStat.submissionCount() == value;
            case DIFFICULTY -> courseStat.avgPoints() == value;
        }).map(CourseStat::course).toArray(Course[]::new);


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
}
