package tracker.Service.DAO;

import tracker.Model.User;

public interface IUserDao extends IDao<User> {
    public User getByEmail(String email);

    void setSubmissionDao(ISubmissionDao submissionDao);
}
