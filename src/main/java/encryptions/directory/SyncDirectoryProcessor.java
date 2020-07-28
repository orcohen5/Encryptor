package encryptions.directory;

import encryptions.IEncryptor;
import entities.ContentType;
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
    public void encrypt(IEncryptor encryptor, String directoryPathToEncrypt, int repetitionsNumber, List<Long> keyList) throws JAXBException, IOException, SAXException {
        File directoryToEncrypt = new File(directoryPathToEncrypt);
        String encryptedDirectoryPath = directoryPathToEncrypt + "\\" + ContentType.Encrypted;
        File encryptedDirectory = new File(encryptedDirectoryPath);
        List<File> filesToEncryptList = new ArrayList(Arrays.asList(directoryToEncrypt.listFiles()));
        List<File> encryptedFilesList = new ArrayList();

        for(File fileToEncrypt : filesToEncryptList) {
            if(IOFileUtil.isValidFile(fileToEncrypt)) {
                File encryptedFile = (File) encryptor.encrypt(fileToEncrypt.getPath(), repetitionsNumber, keyList);
                encryptedFilesList.add(encryptedFile);
            }
        }
    }

    @Override
    public void decrypt(IEncryptor encryptor, String directoryPathToEncrypt) {

    }
}
