package tracker.Service.DAO;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Submission;
import tracker.Model.User;

import java.util.List;

public interface ISubmissionDao extends IDao<Submission> {
    String getMostPopular();

    /**
     * Retrieves the least popular item from the collection.
     * @return The least popular item.
     */
    String getLeastPopular();

    int getEnrollments(Course course);

    List<CourseStat> getCourseStats(ICourseDao courseDao);

    void setUserDao(IUserDao userDao);

    int getPoints(User user, Course course);

    //    record CourseStats(Course course, int enrollments, List<UserStats>)

}
