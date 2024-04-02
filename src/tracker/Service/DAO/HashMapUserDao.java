package tracker.Service.DAO;

import tracker.Model.User;

import java.util.*;

public class HashMapUserDao extends AbstractHashMapDao<User> implements IUserDao {
    final Map<Integer, User> users = super.store;


    public User getByEmail(String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElse(null);
        //jesus we need to do something better than this
    }

    @Override
    public void setSubmissionDao(ISubmissionDao submissionDao) {
    }

    @Override
    public void add(User user) {
        user = new User(IDSequence++, user.getFirstName(), user.getLastName(), user.getEmail());
        update(user);
    }

}
