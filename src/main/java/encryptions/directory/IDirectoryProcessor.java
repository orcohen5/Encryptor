package encryptions.directory;

import encryptions.IEncryptor;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface IDirectoryProcessor {
    void encrypt(IEncryptor encryptor, String directoryPathToEncrypt, int repetitionsNumber, List<Long> keyList) throws JAXBException, IOException, SAXException;
    void decrypt(IEncryptor encryptor, String directoryPathToEncrypt);
}
