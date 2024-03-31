package tracker.Service.DAO;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Stats.PlatformStats;
import tracker.Model.Submission;
import tracker.Model.User;

import java.util.*;
import java.util.stream.Collectors;

public class HashMapSubmissionDao extends AbstractHashMapDao<Submission> implements ISubmissionDao {
    PlatformStats statCache = null;
    IUserDao userDao;
    @Override
    public String getMostPopular() {
        
        return null;
        //todo implement
    }

    private void updateStats() {
        //todo implement
    }

    @Override
    public String getLeastPopular() {
        //todo implement
        return null;
    }

    @Override
    public int getEnrollments(Course course) {
        return (int) store.values().stream()
                .filter(submission -> submission.courseID() == course.getId())
                .map(Submission::userID)
                .distinct()
                .count();

        //todo test
            }

    @Override
    public List<CourseStat> getCourseStats(ICourseDao courseDao) {
        List<CourseStat> list = new ArrayList<>();
        Map<Course, List<Submission>> map = this.getAll().stream().collect(Collectors.groupingBy(x -> courseDao.get(x.courseID())));
        for (Map.Entry<Course, List<Submission>> courseSubmissionPair : map.entrySet()) {
            Course course = courseSubmissionPair.getKey();
            int count = 0;
            int pointTotal = 0;
            int enrollments;
            int avgPoints;
            Set<Integer> userSet = new HashSet<>();
            for (Submission submission : courseSubmissionPair.getValue()) {
                count++;
                userSet.add(submission.userID());
                pointTotal += submission.points();
            }
            avgPoints = pointTotal / count;
            enrollments = userSet.size();
            list.add(new CourseStat(course, avgPoints, count, enrollments));
        }
        return list;
    }
    @Override
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public int getPoints(User user, Course course) {
        return store.values().stream()
                .filter(x -> x.courseID() == course.getId() && x.userID() == user.getId())
                .mapToInt(Submission::points)
                .sum();
    }

}
