package main.encryptions.algorithms;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IEncryptionAlgorithm {
    long getKey();
    List<Long> encrypt(List<Long> sourceContent, long encryptionKey);
    List<Long> decrypt(List<Long> sourceContent, long decryptionKey);
    String getAlgorithmName();
}
