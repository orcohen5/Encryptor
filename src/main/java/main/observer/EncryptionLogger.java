package main.observer;

import main.entities.OperationType;
import org.springframework.stereotype.Component;
import main.utils.IOConsoleUtil;

@Component
public class EncryptionLogger implements EncryptorObserver {

    @Override
    public void encryptionStarted(String fileName) {
        IOConsoleUtil.printMessage("The encryption of " + fileName + " started!\n");
    }

    @Override
    public void encryptionEnded(EncryptionLogEventArgs logEventArgs) {
        IOConsoleUtil.printMessage("The encryption of " + logEventArgs.getFileName() + " with " +
                logEventArgs.getAlgorithmName() + " algorithm took " + logEventArgs.getProcessTime() + " milliseconds.\n" +
                "The encrypted file has been saved in path: " + logEventArgs.getFilePath() + "\n");
    }

    @Override
    public void decryptionStarted(String fileName) {
        IOConsoleUtil.printMessage("The decryption of " + fileName + " started!\n");
    }

    @Override
    public void decryptionEnded(EncryptionLogEventArgs logEventArgs) {
        IOConsoleUtil.printMessage("The decryption of " + logEventArgs.getFileName() + " with " +
                logEventArgs.getAlgorithmName() + " algorithm took " + logEventArgs.getProcessTime() + " milliseconds.\n" +
                "The encrypted file has been saved in path: " + logEventArgs.getFilePath() + "\n");
    }

    @Override
    public void directoryProcessEnded(OperationType operationType, String directoryPath, long processTime) {
        IOConsoleUtil.printMessage("The " + operationType + " of directory in path: " + directoryPath + " finished!\n" +
                "The " + operationType + " took " + processTime + " milliseconds.\n");
    }
}
