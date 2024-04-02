package tracker.Service.DAO;

import tracker.Model.Course;

import java.util.Arrays;

public class HashMapCourseDao extends AbstractHashMapDao<Course> implements ICourseDao {
    @Override
    public void add(Course course) {
        store.put(course.getId(), course);
    }

    public HashMapCourseDao() {
        Arrays.stream(Course.values()).forEach(this::add);
    }
}
