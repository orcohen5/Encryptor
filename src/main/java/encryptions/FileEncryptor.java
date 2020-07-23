package encryptions;

import entities.ContentType;
import entities.EncryptionResult;
import entities.EventType;
import entities.PropertiesReader;
import exceptions.KeyFormatException;
import jaxb.EncryptionResults;
import jaxb.JAXBManager;
import observer.EncryptionLogEventArgs;
import observer.EncryptorObserver;
import utils.AsciiStringConverterUtil;
import utils.IOFileUtil;
import userInterfaces.FileUI;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileEncryptor implements IEncryptor<File> {
    private static final String KEY_LABEL = "key";
    private static final String TXT_KEY_EXTENSION = ".txt";
    private static final int MAX_KEY_VALUE = 3000;
    private RepeatEncryption repeatEncryption;
    private List<EncryptorObserver> observersList;
    //private JAXBManager<EncryptionResults> jaxbManager;

    public FileEncryptor(RepeatEncryption repeatEncryption) {
        this.repeatEncryption = repeatEncryption;
        observersList = new ArrayList();
        //jaxbManager = new JAXBManager<>();
    }

    public void addObserver(EncryptorObserver encryptorObserver) {
        observersList.add(encryptorObserver);
    }

    public void removeObserver(EncryptorObserver encryptorObserver) {
        observersList.remove(encryptorObserver);
    }

    public File encrypt(String filePathToEncrypt, int repetitionsNumber) throws IOException {
        String fileName = IOFileUtil.getFileNameByPath(filePathToEncrypt);
        EncryptionLogEventArgs logEventArgs = new EncryptionLogEventArgs("", fileName, filePathToEncrypt,
                EventType.ENCRYPTION_STARTED, -1);
        notifyEncryptionDecryption(logEventArgs);
        long startTime = System.currentTimeMillis();
        File encryptedFile = encryptFile(filePathToEncrypt, repetitionsNumber);
        long endTime = System.currentTimeMillis();
        logEventArgs = new EncryptionLogEventArgs(repeatEncryption.getAlgorithmName(), fileName,
                encryptedFile.getPath(), EventType.ENCRYPTION_ENDED, endTime - startTime);
        notifyEncryptionDecryption(logEventArgs);
        EncryptionResults encryptionResults = new EncryptionResults("Encryption", repeatEncryption.getAlgorithmName(),
                fileName, encryptedFile.getName(), endTime - startTime);
        try {
            JAXBManager<EncryptionResults> jaxbManager = new JAXBManager<>(encryptionResults);
            jaxbManager.createXMLFromObject();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return encryptedFile;
    }

    public File decrypt(String filePathToDecrypt, String keyFilePath) throws KeyFormatException, IOException {
        String fileName = IOFileUtil.getFileNameByPath(filePathToDecrypt);
        EncryptionLogEventArgs logEventArgs = new EncryptionLogEventArgs("", fileName, filePathToDecrypt,
                EventType.DECRYPTION_STARTED, -1);
        notifyEncryptionDecryption(logEventArgs);
        long startTime = System.currentTimeMillis();
        File decryptedFile = decryptFile(filePathToDecrypt, keyFilePath);
        long endTime = System.currentTimeMillis();
        logEventArgs = new EncryptionLogEventArgs(repeatEncryption.getAlgorithmName(), fileName,
                decryptedFile.getPath(), EventType.DECRYPTION_ENDED, endTime - startTime);
        notifyEncryptionDecryption(logEventArgs);

        EncryptionResults encryptionResults = new EncryptionResults("Decryption", repeatEncryption.getAlgorithmName(),
                fileName, decryptedFile.getName(), endTime - startTime);
        try {
            JAXBManager<EncryptionResults> jaxbManager = new JAXBManager<>(encryptionResults);
            jaxbManager.createXMLFromObject();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return decryptedFile;
    }

    @Override
    public void checkValidContent(String path) throws IOException {
        String requestedContent = PropertiesReader.getPropertyValueAsString("REQUESTED_CONTENT");

        if(!IOFileUtil.isValidFile(new File(path))) {
            throw new IOException("ERROR: Try again! Please enter a valid " + requestedContent);
        }
    }

    private File encryptFile(String filePathToEncrypt, int repetitionsNumber) throws IOException {
        String fileContent = IOFileUtil.readFile(new File(filePathToEncrypt));
        EncryptionResult encryptionResult = repeatEncryption.encrypt(fileContent, repetitionsNumber);
        String encryptedNewData = encryptionResult.getEncryptedContent();
        String encryptionKey = encryptionResult.getEncryptionKey();
        String encryptedNewDataPath = generateNewPath(filePathToEncrypt, ContentType.Encrypted);
        String keyPath = generateNewPath(filePathToEncrypt, ContentType.Key);
        writeToEncryptedDecryptedFile(encryptedNewData, encryptedNewDataPath, ContentType.Encrypted);
        writeToEncryptedDecryptedFile(encryptionKey, keyPath, ContentType.Key);

        return new File(encryptedNewDataPath);
    }

    private File decryptFile(String filePathToDecrypt, String keyFilePath) throws KeyFormatException, IOException {
        List<Long> keysList = getKeyFromKeyFile(new File(keyFilePath));
        String fileContent = IOFileUtil.readFile(new File(filePathToDecrypt));
        String decryptedNewData = repeatEncryption.decrypt(fileContent, keysList);
        String decryptedNewDataPath = generateNewPath(filePathToDecrypt, ContentType.Decrypted);
        writeToEncryptedDecryptedFile(decryptedNewData, decryptedNewDataPath, ContentType.Decrypted);

        return new File(decryptedNewDataPath);
    }

    private void writeToEncryptedDecryptedFile(String newData, String newDataPath, ContentType contentType) throws IOException {
        if(!IOFileUtil.writeToFile(newData, newDataPath)) {
            throw new IOException("ERROR in writing to file!");
        }

        FileUI.passPathToUser(newDataPath, contentType);
    }

    private String generateNewPath(String filePath, ContentType newDataType) {
        String newPath;
        int lastPointIndex = IOFileUtil.getLastDotIndexInPath(filePath);

        if(newDataType == ContentType.Key) {
            newPath = filePath.substring(0, filePath.lastIndexOf('\\') + 1) + KEY_LABEL + TXT_KEY_EXTENSION;
        } else {
            newPath = filePath.substring(0, lastPointIndex) + "_" + newDataType + filePath.substring(lastPointIndex);
        }

        return newPath;
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
