package encryptions.directory;

import encryptions.FileEncryptor;
import entities.OperationType;
import thread.FileEncryptorThread;
import utils.IOFileUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncDirectoryProcessor extends DirectoryProcessor {
    private ExecutorService executorService;

    @Override
    public void encrypt(FileEncryptor encryptor, String directoryPathToEncrypt, List<Long> keyList) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToEncryptList = IOFileUtil.getFilesInDirectory(directoryPathToEncrypt);
        executorService = Executors.newCachedThreadPool();

        for (File fileToEncrypt : filesToEncryptList) {
            if (isValidFile(fileToEncrypt)) {
                FileEncryptorThread fileEncryptorThread = new FileEncryptorThread(encryptor, fileToEncrypt.getPath(), keyList);
                executorService.execute(fileEncryptorThread);
            }
        }

        waitForAllThreadsToFinish();
        long endTime = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1);
        notifyEncryptionDecryption(encryptor, OperationType.Encryption, directoryPathToEncrypt, endTime - startTime);
    }

    @Override
    public void decrypt(FileEncryptor encryptor, String directoryPathToDecrypt) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToDecryptList = IOFileUtil.getFilesInDirectory(directoryPathToDecrypt);
        String keyFilePath = directoryPathToDecrypt + "\\" + getKeyFileName();
        executorService = Executors.newCachedThreadPool();

        for (File fileToDecrypt : filesToDecryptList) {
            if (isValidFile(fileToDecrypt)) {
                FileEncryptorThread fileEncryptorThread = new FileEncryptorThread(encryptor, fileToDecrypt.getPath(), keyFilePath);
                executorService.execute(fileEncryptorThread);
            }
        }

        waitForAllThreadsToFinish();
        long endTime = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1);
        notifyEncryptionDecryption(encryptor, OperationType.Decryption, directoryPathToDecrypt, endTime - startTime);
    }

    private void waitForAllThreadsToFinish() throws InterruptedException {
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new InterruptedException("ERROR: Encryption has been interrupted unexpectedly!");
        }
    }
}