package tracker.Service.DAO;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Stats.PlatformStat;
import tracker.Model.Submission;
import tracker.Model.User;

import java.util.*;
import java.util.stream.Collectors;

public class HashMapSubmissionDao extends AbstractHashMapDao<Submission> implements ISubmissionDao {

    @Override
    public List<PlatformStat> getPlatformStats() {
        List<PlatformStat> list = new ArrayList<>();
        Map<Integer, List<Submission>> map = this.getAll().stream().collect(Collectors.groupingBy(Submission::courseID));//flight landed
        for (Map.Entry<Integer, List<Submission>> courseSubmissionPair : map.entrySet()) {
            int courseID = courseSubmissionPair.getKey();
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
            list.add(new PlatformStat(courseID, avgPoints, count, enrollments));
        }
        return list;
    }

    @Override
    public List<CourseStat> getCourseStats(Course course) {// a question I have? better to box a list? or extend a list?
        List<CourseStat> list = new ArrayList<>();
        Map<Integer, List<Submission>> map = this.getAll().stream().collect(Collectors.groupingBy(Submission::userID));
        for (Map.Entry<Integer, List<Submission>> userListEntry : map.entrySet()) {
            int userID = userListEntry.getKey();
            int points = 0;
            for (Submission submission : userListEntry.getValue()) {
                points += submission.points();
            }
            double percentage = (double) points / course.MAX_POINTS; //test, left off
            //work percentage
            list.add(new CourseStat(userID, points, percentage));
        }
        return list;
    }

    @Override
    public int getPoints(User user, Course course) {
        return store.values().stream()
                .filter(x -> x.courseID() == course.getId() && x.userID() == user.getId())
                .mapToInt(Submission::points)
                .sum();
    }

}
