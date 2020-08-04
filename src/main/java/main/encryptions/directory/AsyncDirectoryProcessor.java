package main.encryptions.directory;

import main.encryptions.IEncryptor;
import main.entities.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import main.thread.FileEncryptorThread;
import main.utils.IOFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Component
@Qualifier("asyncDirectoryProcessor")
public class AsyncDirectoryProcessor extends DirectoryProcessor {
    FileEncryptorThread fileEncryptorThread;

    @Autowired
    public AsyncDirectoryProcessor(FileEncryptorThread fileEncryptorThread) {
        this.fileEncryptorThread = fileEncryptorThread;
    }

    @Override
    public void encrypt(@Autowired IEncryptor iEncryptor, String directoryPathToEncrypt, List<Long> keyList) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToEncryptList = IOFileUtil.getFilesInDirectory(directoryPathToEncrypt);
        List<FileEncryptorThread> threadsList = new ArrayList();

        for (File fileToEncrypt : filesToEncryptList) {
            if (isValidFile(fileToEncrypt)) {
                initFileEncryptorThread(iEncryptor, fileToEncrypt.getPath(), keyList, "",
                        OperationType.Encryption);
                fileEncryptorThread.run();
                threadsList.add(fileEncryptorThread);
            }
        }

        waitForAllThreadsToFinish(threadsList);
        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(iEncryptor, OperationType.Encryption, directoryPathToEncrypt, endTime - startTime);
    }

    @Override
    public void decrypt(@Autowired IEncryptor iEncryptor, String directoryPathToDecrypt) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToDecryptList = IOFileUtil.getFilesInDirectory(directoryPathToDecrypt);
        String keyFilePath = directoryPathToDecrypt + "\\" + getKeyFileName();
        List<FileEncryptorThread> threadsList = new ArrayList();

        for (File fileToDecrypt : filesToDecryptList) {
            if (isValidFile(fileToDecrypt)) {
                initFileEncryptorThread(iEncryptor, fileToDecrypt.getPath(), null, keyFilePath,
                        OperationType.Decryption);
                fileEncryptorThread.run();
                threadsList.add(fileEncryptorThread);
            }
        }

        waitForAllThreadsToFinish(threadsList);
        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(iEncryptor, OperationType.Decryption, directoryPathToDecrypt, endTime - startTime);
    }

    private void initFileEncryptorThread(IEncryptor iEncryptor, String filePath, List<Long> keyList, String keyFilePath, OperationType operationType) {
        fileEncryptorThread.setFileEncryptor(iEncryptor);
        fileEncryptorThread.setFilePath(filePath);

        if(operationType == OperationType.Encryption) {
            fileEncryptorThread.setKeyList(keyList);
            fileEncryptorThread.setOperationType(OperationType.Encryption);
        } else if(operationType == OperationType.Decryption) {
            fileEncryptorThread.setKeyFilePath(keyFilePath);
            fileEncryptorThread.setOperationType(OperationType.Decryption);
        }

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