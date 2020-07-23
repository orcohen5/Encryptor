package encryptions;

import exceptions.KeyFormatException;

import java.io.IOException;

public interface IEncryptor<T> {
    T encrypt(String contentToEncrypt, int repetitionsNumber) throws IOException;
    T decrypt(String contentToDecrypt, String keyContent) throws KeyFormatException, IOException;
    void checkValidContent(String content) throws IOException;
}
