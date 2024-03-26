package tracker;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserInputValidator {


    public static boolean isValidNewUser(String[] input) {
        if (input.length < 3) {
            System.out.println("Incorrect credentials.");
            return false;
        }
        if (!isValidName(input[0])) {
            System.out.println("Incorrect first name.");
            return false;
        }
        if (!isValidLastName(input)) {
            System.out.println("Incorrect last name.");
            return false;
        }
        if (!isValidEmail(input[input.length - 1])) {
            System.out.println("Incorrect email.");
            return false;
        }
        if (!isAvailableEmail(input[input.length - 1])) {
            System.out.println("This email is already taken.");
            return false;

        }
        return true;
    }

    private static boolean isAvailableEmail(String email) {
        return Main.userDao.getByEmail(email) == null;
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PREDICATE.test(email);
    }

    public static boolean isValidName(String name) {
        return NAME_PREDICATE.test(name);
    }


    private static final Predicate<String> EMAIL_PREDICATE = Pattern
            .compile("(\\w+(\\.\\w+)*)+@(\\w+(\\.\\w+)+)")
            .asMatchPredicate();
    static final Predicate<String> NAME_PREDICATE = Pattern
            .compile("[A-za-z]+([-'][A-Za-z]+)*")
            .asMatchPredicate()
            .and(string -> string.length() > 1);
    static final Predicate<String> POINTS_PREDICATE = Pattern
            .compile("\\w+ \\d+ \\d+ \\d+ \\d+")
            .asMatchPredicate();

    public static boolean isValidLastName(String[] input) {
        return !Arrays.stream(input)
                .filter(NAME_PREDICATE)
                .skip(1)
                .collect(Collectors.joining(" "))
                .isBlank();
    }

    public static boolean isValidPoints(String input) {
        return input.matches("\\w+ \\d+ \\d+ \\d+ \\d+");
    }
}
