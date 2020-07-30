package encryptions.directory;

import encryptions.FileEncryptor;
import exceptions.KeyFormatException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface IDirectoryProcessor {
    void encrypt(FileEncryptor encryptor, String directoryPathToEncrypt, List<Long> keyList) throws JAXBException, IOException, SAXException, InterruptedException;
    void decrypt(FileEncryptor encryptor, String directoryPathToDecrypt) throws SAXException, KeyFormatException, JAXBException, IOException, InterruptedException;
}
