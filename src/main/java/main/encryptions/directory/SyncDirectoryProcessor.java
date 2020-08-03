package main.encryptions.directory;

import main.encryptions.IEncryptor;
import main.entities.OperationType;
import main.exceptions.KeyFormatException;
import org.xml.sax.SAXException;
import main.utils.IOFileUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SyncDirectoryProcessor extends DirectoryProcessor {
    @Override
    public void encrypt(IEncryptor encryptor, String directoryPathToEncrypt, List<Long> keyList) throws JAXBException, IOException, SAXException {
        long startTime = System.currentTimeMillis();
        List<File> filesToEncryptList = IOFileUtil.getFilesInDirectory(directoryPathToEncrypt);

        for (File fileToEncrypt : filesToEncryptList) {
            if (isValidFile(fileToEncrypt)) {
                encryptor.encrypt(fileToEncrypt.getPath(), keyList);
            }
        }

        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(encryptor, OperationType.Encryption, directoryPathToEncrypt, endTime - startTime);
    }

    @Override
    public void decrypt(IEncryptor encryptor, String directoryPathToDecrypt) throws SAXException, KeyFormatException, JAXBException, IOException {
        long startTime = System.currentTimeMillis();
        List<File> filesToDecryptList = IOFileUtil.getFilesInDirectory(directoryPathToDecrypt);
        String keyFilePath = directoryPathToDecrypt + "\\" + getKeyFileName();

        for(File fileToDecrypt : filesToDecryptList) {
            if(isValidFile(fileToDecrypt)) {
                encryptor.decrypt(fileToDecrypt.getPath(), keyFilePath);
            }
        }

        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(encryptor, OperationType.Decryption, directoryPathToDecrypt, endTime - startTime);
    }
}
