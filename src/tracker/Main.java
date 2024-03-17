package tracker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        mainMenu();
    }

    private static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {//menu
            Command command = Command.get(scanner.nextLine());

            switch (command) {
                case EXIT -> {
                    System.out.println("Bye!");
                    System.exit(0);
                }
                case UNKNOWN -> System.out.println("Unknown command!");
                case EMPTY -> System.out.println("No input");
            }
        }
    }

    public enum Command {
        EXIT,
        UNKNOWN,
        EMPTY;

        public static Command get(String input) {
            if (input.matches("^\\s*$")) {
                return EMPTY;
            } else {
                try {
                    return Command.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return UNKNOWN;
                }
            }
        }
    }
}
