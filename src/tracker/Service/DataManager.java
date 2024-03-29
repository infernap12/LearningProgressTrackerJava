package tracker.Service;

import tracker.Service.DAO.*;

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


}
