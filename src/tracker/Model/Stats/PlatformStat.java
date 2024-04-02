package tracker.Model.Stats;

public record PlatformStat(int courseID, double avgPoints, int submissionCount, int enrollments) {
    public enum StatType {
        POPULAR,
        ACTIVITY,
        DIFFICULTY
    }
}
