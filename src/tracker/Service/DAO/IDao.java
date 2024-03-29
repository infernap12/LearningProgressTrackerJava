package tracker.Service.DAO;

import java.util.List;

public interface IDao<T> {

    T get(int id);

    List<T> getAll();

    void add(T t);

    boolean update(T t);

    boolean delete(T t);

    T get(String id);
}
