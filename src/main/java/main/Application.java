package main;

import encryptions.FileEncryptor;
import encryptions.IEncryptor;
import encryptions.RepeatEncryption;
import encryptions.algorithms.ShiftMultiplyEncryption;
import entities.PropertiesReader;
import logic.EncryptorManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.SpringBootConfiguration;
import utils.IOConsoleUtil;

import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        /*ConfigurableApplicationContext context = */SpringApplication.run(Application.class, args);
        initializeEncryptorProperties();

        //FileEncryptor fileEncryptor = context.getBean(FileEncryptor.class);
        IEncryptor fileEncryptor = new FileEncryptor(new RepeatEncryption(new ShiftMultiplyEncryption()));
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
