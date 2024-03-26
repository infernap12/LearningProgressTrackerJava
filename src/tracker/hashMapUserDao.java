package tracker;

import java.util.*;

public class hashMapUserDao implements Dao<User> {

    private final Map<Integer, User> users = new HashMap<>();
    private int userIDSequence = 0;

    @Override
    public User get(int id) {
        return users.get(id);
    }
    @Override
    public User get(String id) {
        int intId;
        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return null;
        }
        return users.get(intId);
    }

    @Override
    public User getByEmail(String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElse(null);
        //jesus we need to do something better than this
    }

    @Override
    public List<User> getAll() {
        //isolates the list from the actual store
        return new ArrayList<>(users.values());
    }

    @Override
    public void add(User user) {
        user = new User(userIDSequence++, user.getFirstName(), user.getLastName(), user.getEmail());
        update(user);
    }

    @Override
    public boolean update(User user) {
        try {
            users.put(user.getId(), user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean delete(int id) {
        return delete(get(id));
    }

    @Override
    public boolean delete(User user) {
        return users.remove(user.getId(), user);
    }
}
