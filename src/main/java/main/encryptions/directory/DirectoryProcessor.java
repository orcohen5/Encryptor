package main.encryptions.directory;

import main.encryptions.FileEncryptor;
import main.encryptions.IEncryptor;
import main.entities.OperationType;
import main.observer.EncryptorObserver;
import main.utils.IOFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public abstract class DirectoryProcessor implements IDirectoryProcessor {
    @Value("${KEY_LABEL}")
    private String KEY_LABEL;

    @Value("${TXT_KEY_EXTENSION}")
    private String TXT_KEY_EXTENSION;

    protected String getKeyFileName() {
        return KEY_LABEL + TXT_KEY_EXTENSION;
    }

    protected boolean isValidFile(File file) {
        String fileName = IOFileUtil.getFileNameByPath(file.getPath());

        return IOFileUtil.isValidFile(file) && !fileName.equals(getKeyFileName());
    }

    protected void notifyEncryptionDecryption(IEncryptor encryptor, OperationType operationType, String directoryPath, long processTime) {
        for(EncryptorObserver encryptorObserver : ((FileEncryptor)encryptor).getObserversList()) {
            encryptorObserver.directoryProcessEnded(operationType, directoryPath, processTime);
        }
    }
}
