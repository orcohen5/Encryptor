package logic;

import encryptions.IEncryptor;
import entities.ContentType;
import entities.PropertiesReader;
import exceptions.KeyFormatException;
import userInterfaces.ConsoleUI;
import utils.IOConsoleUtil;

import java.io.IOException;

public class EncryptorManager {
    private static final int ENCRYPTION_OPTION = PropertiesReader.getPropertyValueAsInt("ENCRYPTION_OPTION");
    private static final int DECRYPTION_OPTION = PropertiesReader.getPropertyValueAsInt("DECRYPTION_OPTION");
    private static final int EXIT_OPTION = PropertiesReader.getPropertyValueAsInt("EXIT_OPTION");
    private static final String REQUESTED_CONTENT = PropertiesReader.getPropertyValueAsString("REQUESTED_CONTENT");
    private static final String ENCRYPTOR_TYPE = PropertiesReader.getPropertyValueAsString("ENCRYPTOR_TYPE");
    private IEncryptor encryptor;

    public EncryptorManager(IEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public void activateEncryptor() {
        printMenu();
        int userChoice = ConsoleUI.getValidIntegerFromUser();

        while(userChoice != EXIT_OPTION) {
            handleUserChoice(userChoice);
            printMenu();
            userChoice = ConsoleUI.getValidIntegerFromUser();
        }

        announceFinishEncryptor();
    }

    private void printMenu() {
        IOConsoleUtil.printMessage("Welcome!\n" +
                "Press the number of option you want to choose:\n" +
                ENCRYPTION_OPTION + " - Encryption\n" +
                DECRYPTION_OPTION + " - Decryption\n" +
                "Press " + EXIT_OPTION + " to exit");
    }

    private void handleUserChoice(int userChoice) {
        if (ENCRYPTION_OPTION == userChoice) {
            activateEncryptionProcess();
        } else if (DECRYPTION_OPTION == userChoice) {
            activateDecryptionProcess();
        } else {
            IOConsoleUtil.printErrorMessage("ERROR: Try again! Please press a valid choice.");
        }
    }

    private void activateEncryptionProcess() {
        String contentToEncrypt = getContentForEncryptionDecryption(ContentType.Encrypted);
        int repetitionsNumber = getValidRepetitionsNumber();

        try {
            encryptor.encrypt(contentToEncrypt, repetitionsNumber);
        } catch (IOException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }

    private void activateDecryptionProcess() {
        String contentToDecrypt = getContentForEncryptionDecryption(ContentType.Decrypted);
        String keyContent = getContentForEncryptionDecryption(ContentType.Key);

        try {
            encryptor.decrypt(contentToDecrypt, keyContent);
        } catch (KeyFormatException | IOException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }

    private String getContentForEncryptionDecryption(ContentType contentType) {
        String content = "";
        boolean isValidContent = false;

        while(!isValidContent) {
            try {
                content = ConsoleUI.retrieveContentFromUser(REQUESTED_CONTENT, contentType, ENCRYPTOR_TYPE);
                encryptor.checkValidContent(content);
                isValidContent = true;
            } catch (IOException e) {
                IOConsoleUtil.printErrorMessage(e.getMessage());
            }
        }

        return content;
    }

    private int getValidRepetitionsNumber() {
        int repetitionsNumber = ConsoleUI.retrieveRepetitionsNumberFromUser();

        while(repetitionsNumber <= 0) {
            IOConsoleUtil.printErrorMessage("ERROR: Try again! Please press a valid repetitions number.");
            repetitionsNumber = ConsoleUI.retrieveRepetitionsNumberFromUser();
        }

        return repetitionsNumber;
    }

    private void announceFinishEncryptor() {
        IOConsoleUtil.printMessage("Encryptor has been finished now.");
    }
}
