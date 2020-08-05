package main.thread;

import main.encryptions.IEncryptor;
import main.entities.OperationType;
import main.exceptions.KeyFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import main.utils.IOConsoleUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@Component
@Scope(value = "prototype")
public class EncryptorThread extends Thread {
    private IEncryptor encryptor;
    private String contentPath;
    private List<Long> keyList;
    private String keyPath;
    private OperationType operationType;

    @Autowired
    public EncryptorThread(@Qualifier("fileEncryptor") IEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public IEncryptor getEncryptor() {
        return encryptor;
    }

    public void setEncryptor(IEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String filePath) {
        this.contentPath = filePath;
    }

    public List<Long> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<Long> keyList) {
        this.keyList = keyList;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyFilePath) {
        this.keyPath = keyFilePath;
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
            encrypt();
        } else if(operationType == OperationType.Decryption) {
            decrypt();
        }
    }

    private void encrypt() {
        try {
            encryptor.encrypt(contentPath, keyList);
        } catch (IOException | JAXBException | SAXException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }

    private void decrypt() {
        try {
            encryptor.decrypt(contentPath, keyPath);
        } catch (KeyFormatException | IOException | JAXBException | SAXException e) {
            IOConsoleUtil.printErrorMessage(e.getMessage());
        }
    }
}
