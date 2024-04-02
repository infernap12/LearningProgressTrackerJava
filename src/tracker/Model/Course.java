package tracker.Model;

public enum Course implements Storable {
    JAVA(600),
    DSA(400),
    DATABASES(480),
    SPRING(550);

    public final int MAX_POINTS;

    @Override
    public String toString() {
        if (this == DSA) {
            return "DSA";
        } else {
            return ((char) (this.name().toLowerCase().charAt(0) - 32)) +
                   this.name().toLowerCase().substring(1);

        }
    }

    Course(int maxPoints) {

        this.MAX_POINTS = maxPoints;
    }

    @Override
    public int getId() {
        return this.ordinal();
    }
}
