import encryptions.FileEncryptor;
import encryptions.RepeatEncryption;
import entities.EncryptionResult;
import exceptions.KeyFormatException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.xml.sax.SAXException;
import utils.IOFileUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class FileEncryptorTest {
    private FileEncryptor fileEncryptor;
    private RepeatEncryption repeatEncryption;

    public FileEncryptorTest() {
        repeatEncryption = Mockito.mock(RepeatEncryption.class);
        fileEncryptor = new FileEncryptor(repeatEncryption);
    }

    @Test
    public void encryptTest() throws IOException, JAXBException, SAXException {
        String filePathToEncrypt = getFullPathByRelativePath("files/file.txt");
        int repetitionsNumber = 3;
        String fileContentToEncrypt = getExampleOriginalFileContent();
        List<Long> keyList = getExampleKeysList();
        when(repeatEncryption.encrypt(fileContentToEncrypt, repetitionsNumber, keyList)).thenReturn(new EncryptionResult(
                getExampleEncryptedFileContent(), getExampleKeyString()));
        File encryptedFile = fileEncryptor.encrypt(filePathToEncrypt, repetitionsNumber, keyList);
        String encryptedFileContent = IOFileUtil.readFile(encryptedFile);

        Assert.assertEquals(getExampleEncryptedFileContent(),encryptedFileContent);
    }

    @Test
    public void decryptTest() throws IOException, KeyFormatException, JAXBException, SAXException {
        String filePathToDecrypt = getFullPathByRelativePath("files/file_Encrypted.txt");
        String keyFilePath = getFullPathByRelativePath("files/key.txt");
        String fileContentToDecrypt = getExampleEncryptedFileContent();
        List<Long> keysList = getExampleKeysList();
        when(repeatEncryption.decrypt(fileContentToDecrypt, keysList)).thenReturn(getExampleOriginalFileContent());
        File decryptedFile = fileEncryptor.decrypt(filePathToDecrypt, keyFilePath);
        String decryptedFileContent = IOFileUtil.readFile(decryptedFile);

        Assert.assertEquals(getExampleOriginalFileContent(), decryptedFileContent);
    }

    @Test(expected = IOException.class)
    public void encryptTestWhenFileToEncryptIsEmpty() throws IOException, JAXBException, SAXException {
        String filePathToEncrypt = getFullPathByRelativePath("files/empty.txt");
        int repetitionsNumber = 3;
        List<Long> keysList = getExampleKeysList();
        fileEncryptor.encrypt(filePathToEncrypt, repetitionsNumber, keysList);
    }

    @Test(expected = IOException.class)
    public void decryptTestWhenFileToEncryptIsEmpty() throws IOException, KeyFormatException, JAXBException, SAXException {
        String filePathToDecrypt = getFullPathByRelativePath("files/empty.txt");
        String keyFilePath = getFullPathByRelativePath("files/key.txt");
        fileEncryptor.decrypt(filePathToDecrypt, keyFilePath);
    }

    @Test(expected = IOException.class)
    public void decryptTestWhenKeyFileIsEmpty() throws KeyFormatException, IOException, JAXBException, SAXException {
        String filePathToDecrypt = getFullPathByRelativePath("files/file_Encrypted.txt");
        String keyFilePath = getFullPathByRelativePath("files/empty.txt");
        fileEncryptor.decrypt(filePathToDecrypt, keyFilePath);
    }

    @Test(expected = KeyFormatException.class)
    public void decryptTestWhenPartOfKeyInFileIsOutOfRange() throws KeyFormatException, IOException, JAXBException, SAXException {
        String filePathToDecrypt = getFullPathByRelativePath("files/file_Encrypted.txt");
        String keyFilePath = getFullPathByRelativePath("files/key2.txt");
        fileEncryptor.decrypt(filePathToDecrypt, keyFilePath);
    }

    @Test(expected = KeyFormatException.class)
    public void decryptTestWhenKeyFileContainsUnsupportedChar() throws KeyFormatException, IOException, JAXBException, SAXException {
        String filePathToDecrypt = getFullPathByRelativePath("files/file_Encrypted.txt");
        String keyFilePath = getFullPathByRelativePath("files/key3.txt");
        fileEncryptor.decrypt(filePathToDecrypt, keyFilePath);
    }

    @Test(expected = KeyFormatException.class)
    public void decryptTestWhenKeyFileContainsDoubleComma() throws KeyFormatException, IOException, JAXBException, SAXException {
        String filePathToDecrypt = getFullPathByRelativePath("files/file_Encrypted.txt");
        String keyFilePath = getFullPathByRelativePath("files/key4.txt");
        fileEncryptor.decrypt(filePathToDecrypt, keyFilePath);
    }

    @Test(expected = IOException.class)
    public void checkValidContentTestWhenFileIsEmpty() throws IOException {
        String filePathToCheck = getFullPathByRelativePath("files/empty.txt");
        fileEncryptor.checkValidContent(filePathToCheck);
    }

    private String getFullPathByRelativePath(String relativePath) {
        ClassLoader loader = FileEncryptorTest.class.getClassLoader();
        return loader.getResource(relativePath).getPath();
    }

    private String getExampleOriginalFileContent() {
        return "whats up";
    }

    private String getExampleEncryptedFileContent() {
        return "1408385798820,1230858177120,1148011953660,1372880274480,1361045099700," +
                "378725592960,1384715449260,1325539575360";
    }

    private String getExampleKeyString() {
        return "2535,1878,2486";
    }

    private List<Long> getExampleKeysList() {
        return Arrays.asList(2535L, 1878L, 2486L);
    }


}
