package encryptions.algorithms;

import java.util.List;

public interface IEncryptionAlgorithm {
    long getKey();
    List<Long> encrypt(List<Long> sourceContent);
    List<Long> decrypt(List<Long> sourceContent, long decryptionKey);
    String getAlgorithmName();
}
