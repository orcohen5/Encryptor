package utils;

import java.util.Scanner;

public class IOConsoleUtil {

    private IOConsoleUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
    }

    public static int getIntegerFromUser() throws NumberFormatException{
        String input = getDataFromUser();

        if(!input.chars().allMatch(Character::isDigit)) {
            throw new NumberFormatException("ERROR: Your input is not a valid integer! Please press a number again.");
        }

        return Integer.parseInt(input);
    }

    public static String getDataFromUser() {
        Scanner inputScanner = new Scanner(System.in);
        return inputScanner.nextLine();
    }
}
