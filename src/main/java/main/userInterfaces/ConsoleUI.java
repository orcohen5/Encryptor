package main.userInterfaces;

import main.entities.ContentType;
import org.springframework.stereotype.Component;
import main.utils.IOConsoleUtil;

@Component
public class ConsoleUI {

    private ConsoleUI() {

    }

    public static String retrieveContentFromUser(String requestedContent, ContentType contentType, String encryptorType) {
        IOConsoleUtil.printMessage("Enter " + requestedContent + " for " + encryptorType + " to be " + contentType);
        String content = IOConsoleUtil.getDataFromUser();

        return content;
    }

    public static int retrieveRepetitionsNumberFromUser() throws NumberFormatException {
        IOConsoleUtil.printMessage("Enter repetitions number for " +  ContentType.Encrypted + " content:");

        return getValidIntegerFromUser();
    }

    public static int getValidIntegerFromUser() {
        boolean isInteger = false;
        int integerInput = 0;

        while(!isInteger) {
            try {
                integerInput = IOConsoleUtil.getIntegerFromUser();
                isInteger = true;
            } catch (NumberFormatException e) {
                IOConsoleUtil.printErrorMessage(e.getMessage());
            }
        }

        return integerInput;
    }
}
