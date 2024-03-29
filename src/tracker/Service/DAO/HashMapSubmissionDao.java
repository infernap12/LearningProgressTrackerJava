package tracker.Service.DAO;

import tracker.Model.Course;
import tracker.Model.Submission;
import tracker.Model.User;

public class HashMapSubmissionDao extends AbstractHashMapDao<Submission> implements ISubmissionDao {
    PlatformStats statCache = null;
    IUserDao userDao;
    @Override
    public String getMostPopular() {
        
        return null;
        //todo implement
    }

    private void updateStats() {
        int javaEnrollments = (int) store.values().stream().filter(x-> x.java() > 0).count();
        //todo implement
    }

    @Override
    public String getLeastPopular() {
        //todo implement
        return null;
    }

    @Override
    public int getEnrollments(Course course) {
        //todo test
        return (int) store.values().stream()
                .mapToInt(x -> {
                    switch (course) {
                        case JAVA -> {
                            return x.java();
                        }
                        case DSA -> {
                            return x.DSA();
                        }
                        case DATABASES -> {
                            return x.databases();
                        }
                        case SPRING -> {
                            return x.spring();
                        }
                    }
                    return 0;
                })
                .filter(x -> x > 0)
                .count();
    }

    @Override
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public int getPoints(User user, Course course) {
        return store.values().stream()
                .filter(submission -> submission.userID() == user.getId())
                .mapToInt(submission -> switch (course) {
                    case JAVA -> submission.java();
                    case DSA -> submission.DSA();
                    case DATABASES -> submission.databases();
                    case SPRING -> submission.spring();
        }).sum();
    }

}
