package tracker.Model;

public record Submission(int submissionID, int userID, int java, int DSA, int databases, int spring) implements Storable {

    @Override
    public int getId() {
        return submissionID();
    }

    public Submission(int[] array) {
        this(-1, array[0], array[1], array[2], array[3], array[4]);
        if (array.length != Course.values().length + 1) {
            throw new IllegalArgumentException("Array must be the student ID, followed by an int for each course");
        }
    }
}
