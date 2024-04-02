package tracker.Service.DAO;

import tracker.Model.Storable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractHashMapDao<T extends Storable> implements IDao<T> {

    final Map<Integer, T> store = new HashMap<>();
    int IDSequence = 0;

    @Override
    public T get(int id) {
        return store.get(id);
    }

    @Override
    public List<T> getAll() {
        //isolates the list from the actual store
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean update(T t) {
        T previous = store.put(t.getId(), t);
        return previous != null;
    }

    public boolean delete(int id) {
        return delete(get(id));
    }

    @Override
    public boolean delete(T t) {
        return store.remove(t.getId(), t);
    }

    @Override
    public void add(T t) {
        T previous = store.put(IDSequence++, t);
        if (previous != null) {
            throw new IllegalStateException("ID already in use.");
        }
    }
}
