import encryptions.algorithms.ShiftUpEncryption;
import entities.KeyGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class ShiftUpEncryptionTest {
    private ShiftUpEncryption shiftUpEncryption;

    @Mock
    private KeyGenerator keyGenerator;

    public ShiftUpEncryptionTest() {
        keyGenerator = Mockito.mock(KeyGenerator.class);
        shiftUpEncryption = new ShiftUpEncryption(keyGenerator);
    }

    @Test
    public void encryptTest() {
        List<Long> testCodesList = getExampleOriginalCodesList();
        long key = getExampleKey();
        when(keyGenerator.generateKey()).thenReturn(key);
        List<Long> actualList = shiftUpEncryption.encrypt(testCodesList);
        List<Long> expectedList = getExampleEncryptedCodesList();

        Assert.assertEquals(expectedList,actualList);
    }

    @Test
    public void decryptTest() {
        List<Long> testCodesList = getExampleEncryptedCodesList();
        long key = getExampleKey();
        List<Long> actualList = shiftUpEncryption.decrypt(testCodesList, key);
        List<Long> expectedList = getExampleOriginalCodesList();

        Assert.assertEquals(expectedList,actualList);
    }

    @Test
    public void decryptTestWhenKeyIsZero() {
        List<Long> testCodesList = getExampleEncryptedCodesList();
        shiftUpEncryption.decrypt(testCodesList, 0);
    }


    private List<Long> getExampleOriginalCodesList() {
        return Arrays.asList(119L, 104L, 97L, 116L, 115L, 32L, 117L, 112L);
    }

    private List<Long> getExampleEncryptedCodesList() {
        return Arrays.asList(1619L, 1604L, 1597L, 1616L, 1615L, 1532L, 1617L, 1612L);
    }

    private long getExampleKey() {
        return 1500;
    }
}
