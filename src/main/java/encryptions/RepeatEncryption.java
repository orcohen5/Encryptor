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

    public EncryptionResult encrypt(String sourceContent, int repetitionsNumber, List<Long> keyList) {
        List<Long> codesList = AsciiStringConverterUtil.convertStringToAsciiCodes(sourceContent);
        List<Long> keysList = keyList;

        for(int i = 0; i < repetitionsNumber; i++) {
            codesList = encryptionAlgorithm.encrypt(codesList, keyList.get(i));
            //keysList.add(encryptionAlgorithm.getKey());
        }

        String encryptedContent = AsciiStringConverterUtil.convertAsciiCodesToAsciiCodesString(codesList);
        String keysString = AsciiStringConverterUtil.convertAsciiCodesToAsciiCodesString(keyList);

        return new EncryptionResult(encryptedContent, keysString);
    }

    public String decrypt(String sourceContent, List<Long> keysList) {
        int repetitionsNumber = keysList.size();
        List<Long> codesList = AsciiStringConverterUtil.convertAsciiCodesStringToAsciiCodes(sourceContent);

        for(int i = repetitionsNumber - 1; i >= 0; i--) {
            codesList = encryptionAlgorithm.decrypt(codesList, keysList.get(i));
        }

        return AsciiStringConverterUtil.convertAsciiCodesToString(codesList);
    }
}
