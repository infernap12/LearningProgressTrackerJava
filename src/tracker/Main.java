// https://github.com/infernap12/LearningProgressTrackerJava/ for full project with all packages
package tracker;

import tracker.Model.Stats.CourseStatSummary;
import tracker.Model.Stats.PlatformStatSummary;
import tracker.Service.*;
import tracker.Service.DAO.ISubmissionDao;
import tracker.Service.DAO.IUserDao;
import tracker.Model.Course;
import tracker.Model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static final boolean IS_TEST = false;
    public static final boolean IS_IN_MEMORY = true;
    private static final String TEST_TEXT;
    public static final Scanner SCANNER;

    static {
        TEST_TEXT = """
                add students
                John Doe johnd@email.net
                Jane Spark jspark@yahoo.com
                back
                list
                add points
                0 8 7 7 5
                0 7 6 9 7
                0 6 5 5 0
                1 8 0 8 6
                1 7 0 0 0
                1 9 0 0 5
                back
                statistics
                java
                dsa
                databases
                spring
                back
                exit""";
        SCANNER = IS_TEST ? new Scanner(TEST_TEXT) : new Scanner(System.in);
    }

    static DataManager dataManager = new DataManager(IS_IN_MEMORY);
    static ISubmissionDao submissionDao = dataManager.getSubmissionDao();
    static IUserDao userDao = dataManager.getUserDao();

    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        mainMenu();
    }

    private static void mainMenu() {
        while (true) {//menu
            Command command;
            try {
                command = Command.get(SCANNER.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }

            switch (command) {
                case EXIT -> {
                    System.out.println("Bye!");
                    System.exit(0);
                }
                case ADD_POINTS -> addPointsMenu();
                case ADD_STUDENTS -> addMenu();
                case BACK -> System.out.println("Enter 'exit' to exit the program");
                case FIND -> findMenu();
                case LIST -> {
                    List<User> list = userDao.getAll();
                    if (list.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        System.out.println("Students:");
                        list.forEach(user -> System.out.println(user.getId()));
                    }

                }
                case NOTIFY -> sendNotifications();
                case STATISTICS -> statisticsMenu();
                case HELP -> printHelp();
            }
        }
    }

    private static void sendNotifications() {
        Map<User, List<Course>> users = dataManager.getNotifiableUsers();
        for (Map.Entry<User, List<Course>> entry : users.entrySet()) {
            for (Course course : entry.getValue()) {
                User user = entry.getKey();
                System.out.printf("""
                        To: %s
                        Re: Your Learning Progress
                        Hello, %s! You have accomplished our %s course!%n""", user.getEmail(), user.getFullName(), course.toString());
                user.notified(course);
            }
        }
        System.out.printf("Total %d students have been notified.%n", users.size());
    }

    private static void statisticsMenu() {
        System.out.println("Type the name of a course to see details or 'back' to quit");
        printPlatformStatistics();
        while (true) {
            String input = SCANNER.nextLine().toUpperCase();
            if (input.equalsIgnoreCase("BACK")) {
                return;
            }
            Course course;
            try {
                course = Course.valueOf(input);
                printCourseStats(course);
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown course.");
            }

        }
    }

    private static void printCourseStats(Course course) {
        CourseStatSummary courseStatSummary = dataManager.getCourseStats(course);
        System.out.println(course.toString());
        courseStatSummary.print();
    }

    private static void printPlatformStatistics() {
        PlatformStatSummary platformStatSummary = dataManager.getPlatformStats();
        platformStatSummary.print();
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
                        submissionDao.getPoints(user, Course.JAVA),
                        submissionDao.getPoints(user, Course.DSA),
                        submissionDao.getPoints(user, Course.DATABASES),
                        submissionDao.getPoints(user, Course.SPRING));
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
                User user;
                try {
                    user = userDao.get(Integer.parseInt(arr[0]));
                } catch (NumberFormatException e) {
                    user = null;
                }
                if (user == null) {
                    System.out.printf("No student is found for id=%s%n", arr[0]);
                    continue;
                }
                int[] points = Arrays.stream(arr)
                        .skip(1)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                dataManager.addPoints(user.getId(), points);
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
                userDao.add(new User(credentials[0], credentials[1], credentials[2]));
                count++;
                System.out.println("The student has been added.");
            }
        }


    }

    private static void printHelp() {
        System.out.println("Available commands:");
        for (Command command : Command.values()) {
            System.out.printf("%s - %s%n",
                    command.name().replaceAll("_", " "),
                    command.getDescription());
        }
    }

    public enum Command {
        ADD_POINTS("Open the add points menu"),
        ADD_STUDENTS("Open the add students menu"),
        BACK("Return back to the previous menu"),
        //EMPTY("Debug empty string command"),
        EXIT("Quit program"),
        FIND("Find student"),
        HELP("Print this help message"),
        LIST("List students"),
        NOTIFY("Send completion notifications"),
        STATISTICS("Open statistics menu");
        //UNKNOWN("Debug default error command");

        private final String description;

        Command(String description) {
            this.description = description;
        }

        public static Command get(String input) throws IllegalArgumentException {
            if (input.isBlank()) {
                throw new IllegalArgumentException("No input");
            } else {
                try {
                    return Command.valueOf(input.toUpperCase().trim().replaceAll(" ", "_"));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unknown course.", e);
                }
            }
        }

        public String getDescription() {
            return description;
        }
    }
}
