package encryptions.algorithms;

public class ShiftUpEncryption extends BaseEncryption {

    public ShiftUpEncryption() {
        super();
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
