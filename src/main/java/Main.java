import encryptions.FileEncryptor;
import encryptions.RepeatEncryption;
import encryptions.algorithms.ShiftMultiplyEncryption;
import entities.KeyGenerator;
import entities.PropertiesReader;
import logic.EncryptorManager;
import observer.EncryptionLogger;
import utils.IOConsoleUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        initializeEncryptorProperties();
        FileEncryptor fileEncryptor = new FileEncryptor(new RepeatEncryption(new ShiftMultiplyEncryption(new KeyGenerator())));
        fileEncryptor.addObserver(new EncryptionLogger());
        EncryptorManager encryptorManager = new EncryptorManager(fileEncryptor);
        encryptorManager.activateEncryptor();
    }

    private static void initializeEncryptorProperties() {
        try {
            PropertiesReader.initializeProperties();
        } catch (IOException e) {
            IOConsoleUtil.printErrorMessage("ERROR: properties cannot be read.");
        }
    }
}
