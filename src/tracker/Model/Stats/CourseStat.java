package tracker.Model.Stats;

public record CourseStat(int userID, int points, double completionPercentage) implements Comparable<CourseStat> {

    @Override
    public int compareTo(CourseStat that) {
        int output = Integer.compare(that.points, this.points);
        if (output == 0) output = Integer.compare(this.userID, that.userID);

        return output;
    }
}



