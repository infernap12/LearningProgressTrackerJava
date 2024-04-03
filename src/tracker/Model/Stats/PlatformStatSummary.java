package tracker.Model.Stats;

import tracker.Model.Course;

import java.util.List;
import java.util.stream.Collectors;

public record PlatformStatSummary(List<Course> mostPopular, List<Course> leastPopular,
                                  List<Course> highestActivity, List<Course> lowestActivity,
                                  List<Course> easiestCourse, List<Course> hardestCourse) {
    public void print() {
        System.out.println("Most Popular: " + stringify(mostPopular));
        System.out.println("Least Popular: " + stringify(leastPopular));
        System.out.println("Highest Activity: " + stringify(highestActivity));
        System.out.println("Lowest Activity: " + stringify(lowestActivity));
        System.out.println("Easiest Course: " + stringify(easiestCourse));
        System.out.println("Hardest Course: " + stringify(hardestCourse));
    }

    String stringify(List<Course> courses) {
        String output;
        if (!courses.isEmpty()) {
            output = courses.stream()
                    .map(Course::toString)
                    .collect(Collectors.joining(", "));
        } else {
            output = "n/a";
        }
        return output;
    }
}