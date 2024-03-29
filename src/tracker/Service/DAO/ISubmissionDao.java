package tracker.Service.DAO;

import tracker.Model.Course;
import tracker.Model.Submission;
import tracker.Model.User;

public interface ISubmissionDao extends IDao<Submission> {
    String getMostPopular();

    /**
     * Retrieves the least popular item from the collection.
     * @return The least popular item.
     */
    String getLeastPopular();

    int getEnrollments(Course course);

    void setUserDao(IUserDao userDao);

    int getPoints(User user, Course course);

    record PlatformStats(String mostPopular, String leastPopular,
                         String highestActivity, String lowestActivity,
                         String easiestCourse, String hardestCourse) {}
//    record CourseStats(Course course, int enrollments, List<UserStats>)

}
