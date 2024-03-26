package tracker;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static final Scanner SCANNER = new Scanner(System.in);
    static Dao<User> userDao = new hashMapUserDao();

    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        mainMenu();
    }

    private static void mainMenu() {
        while (true) {//menu
            Command command = Command.get(SCANNER.nextLine());

            switch (command) {
                case EXIT -> {
                    System.out.println("Bye!");
                    System.exit(0);
                }
                case ADD_POINTS -> {
                    addPointsMenu();

                }
                case ADD_STUDENTS -> {
                    addMenu();
                }
                case BACK -> {
                    System.out.println("Enter 'exit' to exit the program");
                }
                case FIND -> {
                    findMenu();
                }
                case LIST -> {
                    List<User> list = userDao.getAll();
                    if (list.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        System.out.println("Students:");
                        list.forEach(user -> System.out.println(user.getId()));
                    }

                }
                case UNKNOWN -> System.out.println("Unknown command!");
                case EMPTY -> System.out.println("No input");
            }
        }
    }

    private static void findMenu() {
        System.out.println("Enter an id or 'back' to return");
        while (true) {
            String input = SCANNER.nextLine();
            if (input.equalsIgnoreCase("BACK")) {
                return;
            }
            User user = userDao.get(Integer.parseInt(input));
            if (user != null) {
                System.out.printf("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d%n",
                        user.getId(),
                        user.getPointsJava(),
                        user.getPointsDataStructures(),
                        user.getPointsDatabase(),
                        user.getPointsSpring());
            } else {
                System.out.printf("message: No student is found for id=%s%n", input);
            }
        }
    }

    private static void addPointsMenu() {
        System.out.println("Enter an id and points or 'back' to return");
        while (true) {
            String input = SCANNER.nextLine();
            if (input.equalsIgnoreCase("BACK")) {
                return;
            }
            if (UserInputValidator.isValidPoints(input)) {
                String[] arr = input.split(" ");
                User user = userDao.get(arr[0]);
                if (user == null) {
                    System.out.printf("No student is found for id=%s%n", arr[0]);
                    continue;
                }

                user.addPoints(Arrays.stream(input.split(" "))
                        .skip(1)
                        .mapToInt(Integer::parseInt)
                        .toArray());
                userDao.update(user);
                System.out.println("Points updated.");
            } else {
                System.out.println("Incorrect points format");
            }
        }
    }

    private static void addMenu() {
        System.out.println("Enter student credentials or 'back' to return");
        int count = 0;
        while (true) {
            String input = SCANNER.nextLine();
            if (input.equalsIgnoreCase("BACK")) {
                System.out.printf("Total %d students have been added.%n", count);
                return;
            }
            String[] credentials = input.split(" ");

            if (UserInputValidator.isValidNewUser(credentials)) {
                userDao.add(new User(credentials));
                count++;
                System.out.println("The student has been added.");
            }
        }


    }

    public enum Command {
        ADD_POINTS,
        ADD_STUDENTS,
        BACK,
        EMPTY,
        EXIT,
        FIND,
        LIST,
        UNKNOWN;

        public static Command get(String input) {
            if (input.matches("^\\s*$")) {
                return EMPTY;
            } else {
                try {
                    return Command.valueOf(input.toUpperCase().trim().replaceAll(" ", "_"));
                } catch (IllegalArgumentException e) {
                    return UNKNOWN;
                }
            }
        }
    }
}
