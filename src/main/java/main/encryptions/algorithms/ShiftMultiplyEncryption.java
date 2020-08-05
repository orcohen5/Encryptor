package main.encryptions.algorithms;

import org.springframework.stereotype.Component;

@Component("shiftMultiplyEncryption")
public class ShiftMultiplyEncryption extends BaseEncryption {

    public ShiftMultiplyEncryption() {
        super();
    }

    @Override
    protected long encryptSingleCode(long codeToEncrypt, long encryptionKey) {
        return codeToEncrypt * encryptionKey;
    }

    @Override
    protected long decryptSingleCode(long codeToDecrypt, long decryptionKey) {
        return codeToDecrypt / decryptionKey;
    }
}
