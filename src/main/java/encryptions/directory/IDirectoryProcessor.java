package encryptions.directory;

import encryptions.IEncryptor;
import exceptions.KeyFormatException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface IDirectoryProcessor {
    void encrypt(IEncryptor encryptor, String directoryPathToEncrypt, List<Long> keyList) throws JAXBException, IOException, SAXException, InterruptedException;
    void decrypt(IEncryptor encryptor, String directoryPathToDecrypt) throws SAXException, KeyFormatException, JAXBException, IOException, InterruptedException;
}
