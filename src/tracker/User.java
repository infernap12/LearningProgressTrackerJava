package tracker;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static tracker.UserInputValidator.NAME_PREDICATE;

public final class User {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private int pointsJava;
    private int pointsDataStructures;
    private int pointsDatabase;
    private int pointsSpring;

    public User(
            int id,
            String firstName,
            String lastName,
            String email,
            int pointsJava,
            int pointsDataStructures,
            int pointsDatabase,
            int pointsSpring
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pointsJava = pointsJava;
        this.pointsDataStructures = pointsDataStructures;
        this.pointsDatabase = pointsDatabase;
        this.pointsSpring = pointsSpring;
    }

    public User(String firstName, String lastName, String email) {
        this(-1, firstName, lastName, email);
    }

    public User(int id, String firstName, String lastName, String email) {
        this(id, firstName, lastName, email, 0, 0, 0, 0);
    }

    public User(String[] creds) {
        this(creds[0],
                Arrays.stream(creds)
                        .filter(NAME_PREDICATE)
                        .skip(1)
                        .collect(Collectors.joining(" ")),
                creds[creds.length - 1]);
    }

    public void addPoints(int[] array) {
        this.pointsJava += array[0];
        this.pointsDataStructures += array[1];
        this.pointsDatabase += array[2];
        this.pointsSpring += array[3];
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public int getPointsJava() {
        return pointsJava;
    }

    public int getPointsDataStructures() {
        return pointsDataStructures;
    }

    public int getPointsDatabase() {
        return pointsDatabase;
    }

    public int getPointsSpring() {
        return pointsSpring;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return this.id == that.id &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.email, that.email) &&
                this.pointsJava == that.pointsJava &&
                this.pointsDataStructures == that.pointsDataStructures &&
                this.pointsDatabase == that.pointsDatabase &&
                this.pointsSpring == that.pointsSpring;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, pointsJava, pointsDataStructures, pointsDatabase, pointsSpring);
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "email=" + email + ", " +
                "pointsJava=" + pointsJava + ", " +
                "pointsDataStructures=" + pointsDataStructures + ", " +
                "pointsDatabase=" + pointsDatabase + ", " +
                "pointsSpring=" + pointsSpring + ']';
    }

}
