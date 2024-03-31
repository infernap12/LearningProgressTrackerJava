package tracker.Model;

public record Submission(int id, int userID, int courseID, int points) implements Storable {

    @Override
    public int getId() {
        return id();
    }


}
