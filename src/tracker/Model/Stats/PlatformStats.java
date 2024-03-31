package tracker.Model.Stats;

import tracker.Model.Course;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public record PlatformStats(Course[] mostPopular, Course[] leastPopular,
                            Course[] highestActivity, Course[] lowestActivity,
                            Course[] easiestCourse, Course[] hardestCourse) {
    public void print() {
        System.out.println("Most Popular: " + stringify.apply(mostPopular));
        System.out.println("Least Popular: " + stringify.apply(leastPopular));
        System.out.println("Highest Activity: " + stringify.apply(highestActivity));
        System.out.println("Lowest Activity: " + stringify.apply(lowestActivity));
        System.out.println("Easiest Course: " + stringify.apply(easiestCourse));
        System.out.println("Hardest Course: " + stringify.apply(hardestCourse));
    }
    static Function<Course[], String> stringify = x -> Arrays.stream(x).map(Course::toString).collect(Collectors.joining(", "));
}
