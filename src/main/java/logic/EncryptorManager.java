package logic;

import encryptions.FileEncryptor;
import encryptions.IEncryptor;
import encryptions.directory.AsyncDirectoryProcessor;
import encryptions.directory.IDirectoryProcessor;
import entities.ContentType;
import entities.KeyGenerator;
import entities.PropertiesReader;
import exceptions.KeyFormatException;
import observer.EncryptionLogger;
import org.xml.sax.SAXException;
import userInterfaces.ConsoleUI;
import utils.IOConsoleUtil;
import utils.IOFileUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
        encryptor.addObserver(new EncryptionLogger());

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
        List<Long> keyList = new KeyGenerator().generateKeyListByRepetitionsNumber(repetitionsNumber);
        IDirectoryProcessor directoryProcessor = new AsyncDirectoryProcessor();

        try {
            directoryProcessor.encrypt(encryptor, contentToEncrypt, keyList);
        } catch (IOException | JAXBException | SAXException | InterruptedException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }

    private void activateDecryptionProcess() {
        String contentToDecrypt = getContentForEncryptionDecryption(ContentType.Decrypted);
        IDirectoryProcessor directoryProcessor = new AsyncDirectoryProcessor();

        try {
            directoryProcessor.decrypt(encryptor, contentToDecrypt);
        } catch (KeyFormatException | IOException | JAXBException | SAXException | InterruptedException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }

    private String getContentForEncryptionDecryption(ContentType contentType) {
        String content = ConsoleUI.retrieveContentFromUser(REQUESTED_CONTENT, contentType, ENCRYPTOR_TYPE);

        while(!IOFileUtil.isValidDirectory(new File(content))) {
            IOConsoleUtil.printErrorMessage("ERROR: Please enter again the " + REQUESTED_CONTENT + " for " + ENCRYPTOR_TYPE);
            content = ConsoleUI.retrieveContentFromUser(REQUESTED_CONTENT, contentType, ENCRYPTOR_TYPE);
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
