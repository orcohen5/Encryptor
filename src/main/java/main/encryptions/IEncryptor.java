package main.encryptions;

import main.exceptions.KeyFormatException;
import main.observer.EncryptorObserver;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@Component
public interface IEncryptor<T> {
    T encrypt(String contentToEncrypt, List<Long> keyList) throws IOException, JAXBException, SAXException;
    T decrypt(String contentToDecrypt, String keyContent) throws KeyFormatException, IOException, JAXBException, SAXException;
    void checkValidContent(String content) throws IOException;
    List<EncryptorObserver> getObserversList();
    void addObserver(EncryptorObserver encryptorObserver);
    void removeObserver(EncryptorObserver encryptorObserver);
}
