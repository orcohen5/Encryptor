package encryptions;

import entities.*;
import exceptions.KeyFormatException;
import jaxb.JAXBManager;
import observer.EncryptionLogEventArgs;
import observer.EncryptorObserver;
import org.xml.sax.SAXException;
import utils.AsciiStringConverterUtil;
import utils.IOFileUtil;
import userInterfaces.FileUI;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileEncryptor implements IEncryptor<File> {
    private static final String KEY_LABEL = PropertiesReader.getPropertyValueAsString("KEY_LABEL");
    private static final String TXT_KEY_EXTENSION = PropertiesReader.getPropertyValueAsString("TXT_KEY_EXTENSION");
    private static final int MAX_KEY_VALUE = PropertiesReader.getPropertyValueAsInt("MAX_KEY_VALUE");
    private RepeatEncryption repeatEncryption;
    private List<EncryptorObserver> observersList;

    public FileEncryptor(RepeatEncryption repeatEncryption) {
        this.repeatEncryption = repeatEncryption;
        observersList = new ArrayList();
    }

    public List<EncryptorObserver> getObserversList() {
        return observersList;
    }

    public void addObserver(EncryptorObserver encryptorObserver) {
        observersList.add(encryptorObserver);
    }

    public void removeObserver(EncryptorObserver encryptorObserver) {
        observersList.remove(encryptorObserver);
    }

    public File encrypt(String filePathToEncrypt, List<Long> keyList) throws IOException, JAXBException, SAXException {
        String fileName = IOFileUtil.getFileNameByPath(filePathToEncrypt);
        EncryptionLogEventArgs logEventArgs = new EncryptionLogEventArgs(OperationType.Encryption, "", fileName, filePathToEncrypt,
                EventType.ENCRYPTION_STARTED, -1);
        notifyEncryptionDecryption(logEventArgs);
        long startTime = System.currentTimeMillis();
        File encryptedFile = encryptFile(filePathToEncrypt, keyList);
        long endTime = System.currentTimeMillis();
        logEventArgs = new EncryptionLogEventArgs(OperationType.Encryption, repeatEncryption.getAlgorithmName(), fileName,
                encryptedFile.getPath(), EventType.ENCRYPTION_ENDED, endTime - startTime);
        notifyEncryptionDecryption(logEventArgs);
        handleXMLWrite(logEventArgs);

        return encryptedFile;
    }

    public File decrypt(String filePathToDecrypt, String keyFilePath) throws KeyFormatException, IOException, JAXBException, SAXException {
        String fileName = IOFileUtil.getFileNameByPath(filePathToDecrypt);
        EncryptionLogEventArgs logEventArgs = new EncryptionLogEventArgs(OperationType.Decryption, "", fileName, filePathToDecrypt,
                EventType.DECRYPTION_STARTED, -1);
        notifyEncryptionDecryption(logEventArgs);
        long startTime = System.currentTimeMillis();
        File decryptedFile = decryptFile(filePathToDecrypt, keyFilePath);
        long endTime = System.currentTimeMillis();
        logEventArgs = new EncryptionLogEventArgs(OperationType.Decryption, repeatEncryption.getAlgorithmName(), fileName,
                decryptedFile.getPath(), EventType.DECRYPTION_ENDED, endTime - startTime);
        notifyEncryptionDecryption(logEventArgs);
        handleXMLWrite(logEventArgs);

        return decryptedFile;
    }

    @Override
    public void checkValidContent(String path) throws IOException {
        String requestedContent = PropertiesReader.getPropertyValueAsString("REQUESTED_CONTENT");

        if(!IOFileUtil.isValidFile(new File(path))) {
            throw new IOException("ERROR: Try again! Please enter a valid " + requestedContent);
        }
    }

    private File encryptFile(String filePathToEncrypt, List<Long> keyList) throws IOException {
        String fileContent = IOFileUtil.readFile(new File(filePathToEncrypt));
        EncryptionResult encryptionResult = repeatEncryption.encrypt(fileContent, keyList);
        String encryptedNewData = encryptionResult.getEncryptedContent();
        String encryptionKey = AsciiStringConverterUtil.convertAsciiCodesToAsciiCodesString(keyList);
        String encryptedNewDataPath = generateNewPath(filePathToEncrypt, ContentType.Encrypted, OperationType.Encryption);
        String keyPath = generateNewPath(filePathToEncrypt, ContentType.Key, OperationType.Encryption);
        writeToEncryptedDecryptedFile(encryptedNewData, encryptedNewDataPath, ContentType.Encrypted);
        writeToEncryptedDecryptedFile(encryptionKey, keyPath, ContentType.Key);

        return new File(encryptedNewDataPath);
    }

    private File decryptFile(String filePathToDecrypt, String keyFilePath) throws KeyFormatException, IOException {
        List<Long> keysList = getKeyFromKeyFile(new File(keyFilePath));
        String fileContent = IOFileUtil.readFile(new File(filePathToDecrypt));
        String decryptedNewData = repeatEncryption.decrypt(fileContent, keysList);
        String decryptedNewDataPath = generateNewPath(filePathToDecrypt, ContentType.Decrypted, OperationType.Decryption);
        String decryptionKey = AsciiStringConverterUtil.convertAsciiCodesToAsciiCodesString(keysList);
        String newKeyPath = generateNewPath(keyFilePath, ContentType.Key, OperationType.Decryption);
        writeToEncryptedDecryptedFile(decryptedNewData, decryptedNewDataPath, ContentType.Decrypted);
        writeToEncryptedDecryptedFile(decryptionKey, newKeyPath, ContentType.Key);

        return new File(decryptedNewDataPath);
    }

    private synchronized void handleXMLWrite(EncryptionLogEventArgs logEventArgs) throws JAXBException, IOException, SAXException {
        String xsdFilePath = "./xmlFile.xsd";
        String xmlFilePath = "./xmlFile.xml";
        JAXBManager<EncryptionLogEventArgs> jaxbManager = new JAXBManager(logEventArgs);
        jaxbManager.createXMLFromObject(xmlFilePath);
        jaxbManager.validateXMLSchema(xsdFilePath,xmlFilePath);
        jaxbManager.createObjectFromXML(xmlFilePath);
    }

    private void writeToEncryptedDecryptedFile(String newData, String newDataPath, ContentType contentType) throws IOException {
        if(!IOFileUtil.writeToFile(newData, newDataPath)) {
            throw new IOException("ERROR in writing to file!");
        }

        if(contentType != ContentType.Key) {
            FileUI.passPathToUser(newDataPath, contentType);
        }

    }

    private String generateNewPath(String filePath, ContentType newDataType, OperationType operationType) {
        String newPath;
        String newDirectoryPath = generateOutputDirectoryPath(filePath, newDataType, operationType);
        File outputDirectory = new File(newDirectoryPath);

        if(!outputDirectory.isDirectory()) {
            outputDirectory.mkdir();
        }

        if(newDataType == ContentType.Key) {
            newPath = newDirectoryPath + "\\" + KEY_LABEL + TXT_KEY_EXTENSION;
        } else {
            newPath = newDirectoryPath + "\\" + IOFileUtil.getFileNameByPath(filePath);
        }

        return newPath;
    }

    private String generateOutputDirectoryPath(String filePath, ContentType newDataType, OperationType operationType) {
        String newDirectoryPath = "";

        if(newDataType == ContentType.Key) {
            if(operationType == OperationType.Encryption) {
                newDirectoryPath = filePath.substring(0, IOFileUtil.getLastSlashIndexInPath(filePath) + 1) +
                        ContentType.Encrypted;
            } else if(operationType == OperationType.Decryption) {
                newDirectoryPath = filePath.substring(0, IOFileUtil.getLastSlashIndexInPath(filePath) + 1) +
                        ContentType.Decrypted;
            }
        } else {
            newDirectoryPath = filePath.substring(0, IOFileUtil.getLastSlashIndexInPath(filePath) + 1) + newDataType;
        }

        return newDirectoryPath;
    }

    private void notifyEncryptionDecryption(EncryptionLogEventArgs logEventArgs) {
        for(EncryptorObserver encryptorObserver : observersList) {
            notifyByEvent(logEventArgs, encryptorObserver);
        }
    }

    private void notifyByEvent(EncryptionLogEventArgs logEventArgs, EncryptorObserver encryptorObserver) {
        switch (logEventArgs.getEventType()) {
            case ENCRYPTION_STARTED:
                encryptorObserver.encryptionStarted(logEventArgs.getFileName());
                break;
            case ENCRYPTION_ENDED:
                encryptorObserver.encryptionEnded(logEventArgs);
                break;
            case DECRYPTION_STARTED:
                encryptorObserver.decryptionStarted(logEventArgs.getFileName());
                break;
            case DECRYPTION_ENDED:
                encryptorObserver.decryptionEnded(logEventArgs);
                break;
        }
    }

    private List<Long> getKeyFromKeyFile(File keyFile) throws IOException, KeyFormatException {
        String key = IOFileUtil.readFile(keyFile);

        if(key.equals("")) {
            throw new IOException("ERROR in reading from key file!");
        }

        return convertKeyToValidFormat(key);
    }


    private List<Long> convertKeyToValidFormat(String key) throws KeyFormatException {
        if(!areAllCharsDigitsOrComma(key) || !areAllKeyPartsInRange(key)) {
            throw new KeyFormatException("ERROR: keys are not in requested format!");
        }

        List<Long> keysList = AsciiStringConverterUtil.convertAsciiCodesStringToAsciiCodes(key);

        return keysList;
    }

    private boolean areAllCharsDigitsOrComma(String key) {
        return key.matches("[0-9,]*") && !key.contains(",,");
    }

    private boolean areAllKeyPartsInRange(String keyString) {
        List<Long> keysList = AsciiStringConverterUtil.convertAsciiCodesStringToAsciiCodes(keyString);
        boolean areAllKeyPartsInRange = true;

        for (long key : keysList) {
            if(key < 0 || key > MAX_KEY_VALUE) {
                areAllKeyPartsInRange = false;
            }
        }

        return areAllKeyPartsInRange;
    }
}
