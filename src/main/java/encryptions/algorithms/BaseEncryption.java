package encryptions.algorithms;

import entities.KeyGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEncryption implements IEncryptionAlgorithm {
    private KeyGenerator keyGenerator;
    private long key;

    public BaseEncryption(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public long getKey() {
        return key;
    }

    @Override
    public List<Long> encrypt(List<Long> codesList) {
        List<Long> codesListToEncrypt = new ArrayList();
        codesListToEncrypt.addAll(codesList);
        key = keyGenerator.generateKey();

        return encryptCodes(codesListToEncrypt);
    }

    @Override
    public List<Long> decrypt(List<Long> codesList, long decryptionKey) {
        List<Long> codesListToDecrypt = new ArrayList();
        codesListToDecrypt.addAll(codesList);
        key = decryptionKey;

        return decryptCodes(codesListToDecrypt);
    }

    private List<Long> encryptCodes(List<Long> codesToEncrypt) {
        for(int i = 0; i < codesToEncrypt.size(); i++) {
            long encryptedCharCode = encryptSingleCode(codesToEncrypt.get(i), key);
            codesToEncrypt.set(i, encryptedCharCode);
        }

        return codesToEncrypt;
    }

    private List<Long> decryptCodes(List<Long> codesToDecrypt) {
        for(int i = 0; i < codesToDecrypt.size(); i++) {
            long decryptedCharCode = decryptSingleCode(codesToDecrypt.get(i), key);
            codesToDecrypt.set(i, decryptedCharCode);
        }

        return codesToDecrypt;
    }

    protected abstract long encryptSingleCode(long codeToEncrypt, long encryptionKey);

    protected abstract long decryptSingleCode(long codeToDecrypt, long decryptionKey);

    @Override
    public String getAlgorithmName() {
        return getClass().getName();
    }

}
