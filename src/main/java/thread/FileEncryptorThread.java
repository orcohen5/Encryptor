package thread;

import encryptions.FileEncryptor;
import entities.OperationType;
import exceptions.KeyFormatException;
import org.xml.sax.SAXException;
import utils.IOConsoleUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public class FileEncryptorThread implements Runnable {
    private FileEncryptor fileEncryptor;
    private String filePath;
    private List<Long> keyList;
    private String keyFilePath;
    private OperationType operationType;

    public FileEncryptorThread(FileEncryptor fileEncryptor, String filePath) {
        this.fileEncryptor = fileEncryptor;
        this.filePath = filePath;
    }

    public FileEncryptorThread(FileEncryptor fileEncryptor, String filePath, List<Long> keyList) {
        this(fileEncryptor, filePath);
        this.keyList = keyList;
        this.operationType = OperationType.Encryption;
    }

    public FileEncryptorThread(FileEncryptor fileEncryptor, String filePath, String keyFilePath) {
        this(fileEncryptor, filePath);
        this.keyFilePath = keyFilePath;
        this.operationType = OperationType.Decryption;
    }

    @Override
    public void run() {
        if(operationType == OperationType.Encryption) {
            encryptFile();
        } else if(operationType == OperationType.Decryption) {
            decryptFile();
        }
    }

    private void encryptFile() {
        try {
            fileEncryptor.encrypt(filePath, keyList);
        } catch (IOException | JAXBException | SAXException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }

    private void decryptFile() {
        try {
            fileEncryptor.decrypt(filePath, keyFilePath);
        } catch (KeyFormatException | IOException | JAXBException | SAXException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }
}
