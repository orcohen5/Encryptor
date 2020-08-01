package observer;

import entities.OperationType;
import org.springframework.stereotype.Component;

@Component
public interface EncryptorObserver {
    void encryptionStarted(String filePath);
    void encryptionEnded(EncryptionLogEventArgs encryptionLogEventArgs);
    void decryptionStarted(String filePath);
    void decryptionEnded(EncryptionLogEventArgs encryptionLogEventArgs);
    void directoryProcessEnded(OperationType operationType, String directoryPath, long processTime);
}
