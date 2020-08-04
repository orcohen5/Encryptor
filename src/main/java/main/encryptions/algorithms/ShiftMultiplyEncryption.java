package main.encryptions.algorithms;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("shiftMultiplyEncryption")
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
