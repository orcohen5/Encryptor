package main.thread;

import main.encryptions.IEncryptor;
import main.entities.OperationType;
import main.exceptions.KeyFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import main.utils.IOConsoleUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@Component
@Scope(value = "prototype")
public class FileEncryptorThread extends Thread {
    private IEncryptor fileEncryptor;
    private String filePath;
    private List<Long> keyList;
    private String keyFilePath;
    private OperationType operationType;

    @Autowired
    public FileEncryptorThread() {

    }

    public IEncryptor getFileEncryptor() {
        return fileEncryptor;
    }

    public void setFileEncryptor(IEncryptor fileEncryptor) {
        this.fileEncryptor = fileEncryptor;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Long> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<Long> keyList) {
        this.keyList = keyList;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
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
