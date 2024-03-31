package tracker.Model.Stats;

import tracker.Model.Course;

import java.util.Arrays;
import java.util.stream.Collectors;

public record PlatformStatSummary(Course[] mostPopular, Course[] leastPopular,
                                  Course[] highestActivity, Course[] lowestActivity,
                                  Course[] easiestCourse, Course[] hardestCourse) {
    public void print() {
        System.out.println("Most Popular: " + stringify(mostPopular));
        System.out.println("Least Popular: " + stringify(leastPopular));
        System.out.println("Highest Activity: " + stringify(highestActivity));
        System.out.println("Lowest Activity: " + stringify(lowestActivity));
        System.out.println("Easiest Course: " + stringify(easiestCourse));
        System.out.println("Hardest Course: " + stringify(hardestCourse));
    }

    String stringify(Course[] courses) {
        String output;
        if (courses.length > 0) {
            output = Arrays.stream(courses)
                    .map(Course::toString)
                    .collect(Collectors.joining(", "));
        } else {
            output = "n/a";
        }
        return output;
    }
}