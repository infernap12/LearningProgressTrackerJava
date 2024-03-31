package tracker.Model.Stats;

import java.util.List;

public record CourseStatSummary(List<CourseStat> courseStats) {
    public void print() {
        System.out.println("%-6s%-10s%-9s%n".formatted("id", "points", "completed").trim());
        String format = "%-6d%-10d%-9s";
        for (CourseStat cs : courseStats) {
            System.out.println(format.formatted(cs.userID(), cs.points(), "%.1f%%".formatted(cs.completionPercentage())).trim());
        }
    }
}
