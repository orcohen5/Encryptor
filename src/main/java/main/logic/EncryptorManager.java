package main.logic;

import main.encryptions.IEncryptor;
import main.encryptions.directory.IDirectoryProcessor;
import main.entities.ContentType;
import main.entities.KeyGenerator;
import main.exceptions.KeyFormatException;
import main.observer.EncryptionLogger;
import main.userInterfaces.ConsoleUI;
import main.utils.IOConsoleUtil;
import main.utils.IOFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

//@PropertySource("src/main/resources/application.properties")
@Component
public class EncryptorManager {
    @Value("${ENCRYPTION_OPTION}")
    private int ENCRYPTION_OPTION;

    @Value("${DECRYPTION_OPTION}")
    private int DECRYPTION_OPTION;

    @Value("${EXIT_OPTION}")
    private int EXIT_OPTION;

    @Value("${REQUESTED_CONTENT}")
    private String REQUESTED_CONTENT;

    @Value("${ENCRYPTOR_TYPE}")
    private String ENCRYPTOR_TYPE;

    @Autowired
    private IEncryptor encryptor;

    @Autowired
    public EncryptionLogger encryptionLogger;

    @Autowired
    public KeyGenerator keyGenerator;

    @Autowired
    @Qualifier("asyncDirectoryProcessor")
    private IDirectoryProcessor directoryProcessor;

    public EncryptorManager() {

    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void activateEncryptor() {
        printMenu();
        int userChoice = ConsoleUI.getValidIntegerFromUser();
        encryptor.addObserver(encryptionLogger);

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
        List<Long> keyList = keyGenerator.generateKeyListByRepetitionsNumber(repetitionsNumber);
        //IDirectoryProcessor directoryProcessor = new AsyncDirectoryProcessor(new FileEncryptorThread());

        try {
            directoryProcessor.encrypt(encryptor, contentToEncrypt, keyList);
        } catch (IOException | JAXBException | SAXException | InterruptedException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }

    private void activateDecryptionProcess() {
        String contentToDecrypt = getContentForEncryptionDecryption(ContentType.Decrypted);
        //IDirectoryProcessor directoryProcessor = new AsyncDirectoryProcessor(new FileEncryptorThread());

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
