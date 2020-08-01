package encryptions.algorithms;

import org.springframework.stereotype.Component;

@Component
public class ShiftMultiplyEncryption extends BaseEncryption {

    public ShiftMultiplyEncryption() {
        super();
    }

//    @Override
//    public String getAlgorithmName() {
//        return getClass().getName();
//    }

    @Override
    protected long encryptSingleCode(long codeToEncrypt, long encryptionKey) {
        return codeToEncrypt * encryptionKey;
    }

    @Override
    protected long decryptSingleCode(long codeToDecrypt, long decryptionKey) {
        return codeToDecrypt / decryptionKey;
    }
}
