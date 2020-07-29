package encryptions.directory;

import encryptions.IEncryptor;
import entities.ContentType;
import entities.PropertiesReader;
import exceptions.KeyFormatException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.IOFileUtil;

public class SyncDirectoryProcessor implements IDirectoryProcessor {
    @Override
    public List<File> encrypt(IEncryptor encryptor, String directoryPathToEncrypt, List<Long> keyList) throws JAXBException, IOException, SAXException {
        File directoryToEncrypt = new File(directoryPathToEncrypt);
        List<File> filesToEncryptList = new ArrayList(Arrays.asList(directoryToEncrypt.listFiles()));
        List<File> encryptedFilesList = new ArrayList();

        for(File fileToEncrypt : filesToEncryptList) {
            if(IOFileUtil.isValidFile(fileToEncrypt) &&
                    !IOFileUtil.getFileNameByPath(fileToEncrypt.getPath()).equals("key.txt")) {
                File encryptedFile = (File) encryptor.encrypt(fileToEncrypt.getPath(), keyList);
                encryptedFilesList.add(encryptedFile);
            }
        }

        return encryptedFilesList;
    }

    @Override
    public List<File> decrypt(IEncryptor encryptor, String directoryPathToDecrypt) throws SAXException, KeyFormatException, JAXBException, IOException {
        File directoryToDecrypt = new File(directoryPathToDecrypt);
        List<File> filesToDecryptList = new ArrayList(Arrays.asList(directoryToDecrypt.listFiles()));
        List<File> decryptedFilesList = new ArrayList();
        String keyFilePath = directoryPathToDecrypt + "\\" +
                PropertiesReader.getPropertyValueAsString("KEY_LABEL") +
                PropertiesReader.getPropertyValueAsString("TXT_KEY_EXTENSION");

        for(File fileToDecrypt : filesToDecryptList) {
            if(IOFileUtil.isValidFile(fileToDecrypt) && !keyFilePath.equals(fileToDecrypt.getPath())) {
                File decryptedFile = (File) encryptor.decrypt(fileToDecrypt.getPath(), keyFilePath);
                decryptedFilesList.add(decryptedFile);
            }
        }

        return decryptedFilesList;
    }
}
