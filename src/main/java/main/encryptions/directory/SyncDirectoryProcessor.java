package main.encryptions.directory;

import main.encryptions.IEncryptor;
import main.entities.OperationType;
import main.exceptions.KeyFormatException;
import main.utils.IOFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;
@Component("syncDirectoryProcessor")
public class SyncDirectoryProcessor extends DirectoryProcessor {
    private IEncryptor encryptor;

    @Autowired
    public SyncDirectoryProcessor(@Qualifier("fileEncryptor") IEncryptor encryptor) {
        this.encryptor = encryptor;
    }


    @Override
    public void encrypt(String directoryPathToEncrypt, List<Long> keyList) throws JAXBException, IOException, SAXException {
        long startTime = System.currentTimeMillis();
        List<File> filesToEncryptList = IOFileUtil.getFilesInDirectory(directoryPathToEncrypt);

        for (File fileToEncrypt : filesToEncryptList) {
            if (isValidFile(fileToEncrypt)) {
                encryptor.encrypt(fileToEncrypt.getPath(), keyList);
            }
        }

        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(OperationType.Encryption, directoryPathToEncrypt, endTime - startTime);
    }

    @Override
    public void decrypt(String directoryPathToDecrypt) throws SAXException, KeyFormatException, JAXBException, IOException {
        long startTime = System.currentTimeMillis();
        List<File> filesToDecryptList = IOFileUtil.getFilesInDirectory(directoryPathToDecrypt);
        String keyFilePath = directoryPathToDecrypt + "\\" + retrieveKeyName();

        for(File fileToDecrypt : filesToDecryptList) {
            if(isValidFile(fileToDecrypt)) {
                encryptor.decrypt(fileToDecrypt.getPath(), keyFilePath);
            }
        }

        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(OperationType.Decryption, directoryPathToDecrypt, endTime - startTime);
    }
}
