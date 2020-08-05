package main.encryptions.directory;

import main.entities.OperationType;
import main.thread.EncryptorThread;
import main.utils.IOFileUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component("asyncDirectoryProcessor")
public class AsyncDirectoryProcessor extends DirectoryProcessor {
    private EncryptorThread encryptorThread;
    private ExecutorService executorService;
    private BeanFactory beanFactory;

    @Autowired
    public AsyncDirectoryProcessor(EncryptorThread encryptorThread, BeanFactory beanFactory) {
        this.encryptorThread = encryptorThread;
        this.beanFactory = beanFactory;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void encrypt(String directoryPathToEncrypt, List<Long> keyList) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToEncryptList = IOFileUtil.getFilesInDirectory(directoryPathToEncrypt);

        for (File fileToEncrypt : filesToEncryptList) {
            if (isValidFile(fileToEncrypt)) {
                encryptorThread = beanFactory.getBean(EncryptorThread.class);
                encryptorThread.setContentPath(fileToEncrypt.getPath());
                encryptorThread.setKeyList(keyList);
                encryptorThread.setOperationType(OperationType.Encryption);
                executorService.execute(encryptorThread);
            }
        }

        waitForAllThreadsToFinish();
        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(OperationType.Encryption, directoryPathToEncrypt,
                endTime - TimeUnit.SECONDS.toMillis(1) - startTime);
    }

    @Override
    public void decrypt(String directoryPathToDecrypt) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToDecryptList = IOFileUtil.getFilesInDirectory(directoryPathToDecrypt);
        String keyPath = directoryPathToDecrypt + "\\" + retrieveKeyName();

        for (File fileToDecrypt : filesToDecryptList) {
            if (isValidFile(fileToDecrypt)) {
                encryptorThread = beanFactory.getBean(EncryptorThread.class);
                encryptorThread.setContentPath(fileToDecrypt.getPath());
                encryptorThread.setKeyPath(keyPath);
                encryptorThread.setOperationType(OperationType.Decryption);
                executorService.execute(encryptorThread);
            }
        }

        waitForAllThreadsToFinish();
        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(OperationType.Decryption, directoryPathToDecrypt,
                endTime - TimeUnit.SECONDS.toMillis(1) - startTime);
    }

    private void waitForAllThreadsToFinish() throws InterruptedException {
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new InterruptedException("ERROR: Encryption has been interrupted unexpectedly!");
        }
    }

}