package tracker.Service.DAO;

import tracker.Model.Course;
import tracker.Model.Stats.CourseStat;
import tracker.Model.Stats.PlatformStat;
import tracker.Model.Submission;
import tracker.Model.User;

import java.util.List;

public interface ISubmissionDao extends IDao<Submission> {

    List<PlatformStat> getPlatformStats();

    int getPoints(User user, Course course);

    List<CourseStat> getCourseStats(Course course);

}
