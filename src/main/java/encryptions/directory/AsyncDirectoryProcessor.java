package encryptions.directory;

import encryptions.FileEncryptor;
import encryptions.IEncryptor;
import entities.OperationType;
import thread.FileEncryptorThread;
import utils.IOFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncDirectoryProcessor extends DirectoryProcessor {

    @Override
    public void encrypt(IEncryptor encryptor, String directoryPathToEncrypt, List<Long> keyList) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToEncryptList = IOFileUtil.getFilesInDirectory(directoryPathToEncrypt);
        List<FileEncryptorThread> threadsList = new ArrayList();

        for (File fileToEncrypt : filesToEncryptList) {
            if (isValidFile(fileToEncrypt)) {
                FileEncryptorThread fileEncryptorThread = new FileEncryptorThread((FileEncryptor) encryptor,
                        fileToEncrypt.getPath(), keyList);
                fileEncryptorThread.start();
                threadsList.add(fileEncryptorThread);
            }
        }

        waitForAllThreadsToFinish(threadsList);
        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(encryptor, OperationType.Encryption, directoryPathToEncrypt, endTime - startTime);
    }

    @Override
    public void decrypt(IEncryptor encryptor, String directoryPathToDecrypt) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToDecryptList = IOFileUtil.getFilesInDirectory(directoryPathToDecrypt);
        String keyFilePath = directoryPathToDecrypt + "\\" + getKeyFileName();
        List<FileEncryptorThread> threadsList = new ArrayList();

        for (File fileToDecrypt : filesToDecryptList) {
            if (isValidFile(fileToDecrypt)) {
                FileEncryptorThread fileEncryptorThread = new FileEncryptorThread((FileEncryptor) encryptor,
                        fileToDecrypt.getPath(), keyFilePath);
                fileEncryptorThread.start();
                threadsList.add(fileEncryptorThread);
            }
        }

        waitForAllThreadsToFinish(threadsList);
        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(encryptor, OperationType.Decryption, directoryPathToDecrypt, endTime - startTime);
    }

    private void waitForAllThreadsToFinish(List<FileEncryptorThread> threadsList) throws InterruptedException {
        for (Thread thread : threadsList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new InterruptedException("ERROR: Encryption has been interrupted unexpectedly!");
            }
        }
    }
}