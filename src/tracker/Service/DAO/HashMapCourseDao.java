package tracker.Service.DAO;

import tracker.Model.Course;

public class HashMapCourseDao extends AbstractHashMapDao<Course> implements ICourseDao {
    @Override
    public void add(Course course) {
        store.put(course.getId(), course);
    }
}
