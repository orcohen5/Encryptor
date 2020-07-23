package encryptions.algorithms;

import entities.KeyGenerator;

public class ShiftUpEncryption extends BaseEncryption {

    public ShiftUpEncryption(KeyGenerator keyGenerator) {
        super(keyGenerator);
    }

    @Override
    public String getAlgorithmName() {
        return getClass().getName();
    }

    @Override
    protected long encryptSingleCode(long codeToEncrypt, long encryptionKey) {
        return codeToEncrypt + encryptionKey;
    }

    @Override
    protected long decryptSingleCode(long codeToDecrypt, long decryptionKey) {
        return codeToDecrypt - decryptionKey;
    }
}
