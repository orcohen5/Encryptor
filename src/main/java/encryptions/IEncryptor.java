package encryptions;

import exceptions.KeyFormatException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface IEncryptor<T> {
    T encrypt(String contentToEncrypt, int repetitionsNumber) throws IOException, JAXBException, SAXException;
    T decrypt(String contentToDecrypt, String keyContent) throws KeyFormatException, IOException, JAXBException, SAXException;
    void checkValidContent(String content) throws IOException;
}
