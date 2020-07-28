package encryptions;

import exceptions.KeyFormatException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface IEncryptor<T> {
    T encrypt(String contentToEncrypt, int repetitionsNumber, List<Long> keyList) throws IOException, JAXBException, SAXException;
    T decrypt(String contentToDecrypt, String keyContent) throws KeyFormatException, IOException, JAXBException, SAXException;
    void checkValidContent(String content) throws IOException;
}
