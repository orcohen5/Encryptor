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
public class AsyncDirectoryProcessor extends DirectoryProcessor /*implements ApplicationContextAware*/ {
    FileEncryptorThread fileEncryptorThread;
    //private ApplicationContext applicationContext;
    //@Autowired
    //private BeanFactory beanFactory;
    @Autowired
    public AsyncDirectoryProcessor(FileEncryptorThread fileEncryptorThread) {
        this.fileEncryptorThread = fileEncryptorThread;
    }

    /*@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }*/

    @Override
    public void encrypt(@Autowired IEncryptor iEncryptor, String directoryPathToEncrypt, List<Long> keyList) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<File> filesToEncryptList = IOFileUtil.getFilesInDirectory(directoryPathToEncrypt);
        List<FileEncryptorThread> threadsList = new ArrayList();

        for (File fileToEncrypt : filesToEncryptList) {
            if (isValidFile(fileToEncrypt)) {
                //FileEncryptorThread fileEncryptorThread = new FileEncryptorThread((FileEncryptor) iEncryptor,
                        //fileToEncrypt.getPath(), keyList);
                //FileEncryptorThread fileEncryptorThread = beanFactory.getBean(FileEncryptorThread.class, iEncryptor, fileToEncrypt.getPath(), keyList);
                fileEncryptorThread.setFileEncryptor(iEncryptor);
                fileEncryptorThread.setFilePath(fileToEncrypt.getPath());
                fileEncryptorThread.setKeyList(keyList);
                fileEncryptorThread.setOperationType(OperationType.Encryption);
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
                /*FileEncryptorThread fileEncryptorThread = new FileEncryptorThread((FileEncryptor) iEncryptor,
                        fileToDecrypt.getPath(), keyFilePath);
                fileEncryptorThread.start();
                threadsList.add(fileEncryptorThread);*/
                fileEncryptorThread.setFileEncryptor(iEncryptor);
                fileEncryptorThread.setFilePath(fileToDecrypt.getPath());
                fileEncryptorThread.setKeyFilePath(keyFilePath);
                fileEncryptorThread.setOperationType(OperationType.Decryption);
                fileEncryptorThread.run();
                threadsList.add(fileEncryptorThread);
            }
        }

        waitForAllThreadsToFinish(threadsList);
        long endTime = System.currentTimeMillis();
        notifyEncryptionDecryption(iEncryptor, OperationType.Decryption, directoryPathToDecrypt, endTime - startTime);
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