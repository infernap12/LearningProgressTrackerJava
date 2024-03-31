package tracker.Model.Stats;

import tracker.Model.Course;

public record PlatformStat(Course course, double avgPoints, int submissionCount, int enrollments) {
    public enum StatType {
        POPULAR,
        ACTIVITY,
        DIFFICULTY;
    }
}
