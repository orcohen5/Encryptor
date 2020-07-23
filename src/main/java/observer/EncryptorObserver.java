package observer;

public interface EncryptorObserver {
    void encryptionStarted(String filePath);
    void encryptionEnded(EncryptionLogEventArgs encryptionLogEventArgs);
    void decryptionStarted(String filePath);
    void decryptionEnded(EncryptionLogEventArgs encryptionLogEventArgs);
}
