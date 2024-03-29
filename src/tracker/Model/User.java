package tracker.Model;

import java.util.Objects;

public final class User implements Storable {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private boolean notifiedJava;
    private boolean notifiedDataStructures;
    private boolean notifiedDatabase;
    private boolean notifiedSpring;


    public User(String firstName, String lastName, String email) {
        this(-1, firstName, lastName, email);
    }

    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
@Override
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email);
    }
}
