package main.encryptions.directory;

import main.exceptions.KeyFormatException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface IDirectoryProcessor {
    void encrypt(String directoryPathToEncrypt, List<Long> keyList) throws JAXBException, IOException, SAXException, InterruptedException;
    void decrypt(String directoryPathToDecrypt) throws SAXException, KeyFormatException, JAXBException, IOException, InterruptedException;
}
