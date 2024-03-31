package tracker.Service.DAO;

import tracker.Model.User;

import java.util.*;

public class HashMapUserDao extends AbstractHashMapDao<User> implements IUserDao {
      final Map<Integer, User> users = super.store;


//    private final Map<Integer, User> users = new HashMap<>();
//    private int IDSequence = 0;
//    @Override
//    public User get(int id) {
//        return users.get(id);
//    }
//    @Override
//    public User get(String id) {
//        int intId;
//        try {
//            intId = Integer.parseInt(id);
//        } catch (NumberFormatException e) {
//            return null;
//        }
//        return users.get(intId);
//    }

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

    /*  @Override
      public List<User> getAll() {
          //isolates the list from the actual store
          return new ArrayList<>(users.values());
      }
  */
    @Override
    public void add(User user) {
        user = new User(IDSequence++, user.getFirstName(), user.getLastName(), user.getEmail());
        update(user);
    }

//    @Override
//    public boolean update(User user) {
//        try {
//            users.put(user.getId(), user);
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

//    public boolean delete(int id) {
//        return delete(get(id));
//    }

//    @Override
//    public boolean delete(User user) {
//        return users.remove(user.getId(), user);
//    }
}
