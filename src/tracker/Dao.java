package tracker;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T get(int id);

    List<T> getAll();

    void add(T t);

    boolean update(T t);

    boolean delete(T t);

    User get(String id);

    T getByEmail(String email);
}
