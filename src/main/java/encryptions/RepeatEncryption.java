package encryptions;

import encryptions.algorithms.IEncryptionAlgorithm;
import entities.EncryptionResult;
import utils.AsciiStringConverterUtil;

import java.util.ArrayList;
import java.util.List;

public class RepeatEncryption {
    private IEncryptionAlgorithm encryptionAlgorithm;

    public RepeatEncryption(IEncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getAlgorithmName() {
        return encryptionAlgorithm.getAlgorithmName();
    }

    public EncryptionResult encrypt(String sourceContent, List<Long> keyList) {
        List<Long> codesList = AsciiStringConverterUtil.convertStringToAsciiCodes(sourceContent);

        for(long keyPart : keyList) {
            codesList = encryptionAlgorithm.encrypt(codesList, keyPart);
        }

        String encryptedContent = AsciiStringConverterUtil.convertAsciiCodesToAsciiCodesString(codesList);
        String keysString = AsciiStringConverterUtil.convertAsciiCodesToAsciiCodesString(keyList);

        return new EncryptionResult(encryptedContent, keysString);
    }

    public String decrypt(String sourceContent, List<Long> keyList) {
        List<Long> codesList = AsciiStringConverterUtil.convertAsciiCodesStringToAsciiCodes(sourceContent);

        for(long keyPart : keyList) {
            codesList = encryptionAlgorithm.decrypt(codesList, keyPart);
        }

        return AsciiStringConverterUtil.convertAsciiCodesToString(codesList);
    }
}
