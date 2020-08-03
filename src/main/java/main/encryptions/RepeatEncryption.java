package main.encryptions;

import main.encryptions.algorithms.IEncryptionAlgorithm;
import main.entities.EncryptionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import main.utils.AsciiStringConverterUtil;

import java.util.List;
@Component
public class RepeatEncryption {
    private IEncryptionAlgorithm encryptionAlgorithm;

    @Autowired
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
