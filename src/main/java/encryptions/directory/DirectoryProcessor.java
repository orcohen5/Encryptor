package encryptions.directory;

import encryptions.FileEncryptor;
import entities.OperationType;
import entities.PropertiesReader;
import observer.EncryptorObserver;
import utils.IOFileUtil;

import java.io.File;

public abstract class DirectoryProcessor implements IDirectoryProcessor {
    protected String getKeyFileName() {
        return PropertiesReader.getPropertyValueAsString("KEY_LABEL") +
                PropertiesReader.getPropertyValueAsString("TXT_KEY_EXTENSION");
    }

    protected boolean isValidFile(File file) {
        String fileName = IOFileUtil.getFileNameByPath(file.getPath());

        return IOFileUtil.isValidFile(file) && !fileName.equals(getKeyFileName());
    }

    protected void notifyEncryptionDecryption(FileEncryptor encryptor, OperationType operationType, String directoryPath, long processTime) {
        for(EncryptorObserver encryptorObserver : encryptor.getObserversList()) {
            encryptorObserver.directoryProcessEnded(operationType, directoryPath, processTime);
        }
    }
}
