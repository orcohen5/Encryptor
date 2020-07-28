import encryptions.algorithms.ShiftMultiplyEncryption;
import entities.KeyGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class ShiftMultiplyEncryptionTest {

    private ShiftMultiplyEncryption shiftMultiplyEncryption;

    @Mock
    private KeyGenerator keyGenerator;

    public ShiftMultiplyEncryptionTest() {
        keyGenerator = Mockito.mock(KeyGenerator.class);
        shiftMultiplyEncryption = new ShiftMultiplyEncryption(keyGenerator);
    }

    @Test
    public void encryptTest() {
        List<Long> testCodesList = getExampleOriginalCodesList();

        long key = getExampleKey();
        when(keyGenerator.generateKey()).thenReturn(key);
        List<Long> actualList = shiftMultiplyEncryption.encrypt(testCodesList, key);
        List<Long> expectedList = getExampleEncryptedCodesList();

        Assert.assertEquals(expectedList,actualList);
    }

    @Test
    public void decryptTest() {
        List<Long> testCodesList = getExampleEncryptedCodesList();
        long key = getExampleKey();
        List<Long> actualList = shiftMultiplyEncryption.decrypt(testCodesList, key);
        List<Long> expectedList = getExampleOriginalCodesList();

        Assert.assertEquals(expectedList,actualList);
    }

    @Test(expected = ArithmeticException.class)
    public void decryptTestWhenKeyIsZero() {
        List<Long> testCodesList = getExampleEncryptedCodesList();
        shiftMultiplyEncryption.decrypt(testCodesList, 0);
    }


    private List<Long> getExampleOriginalCodesList() {
        return Arrays.asList(119L, 104L, 97L, 116L, 115L, 32L, 117L, 112L);
    }

    private List<Long> getExampleEncryptedCodesList() {
        return Arrays.asList(178500L, 156000L, 145500L, 174000L, 172500L, 48000L, 175500L, 168000L);
    }

    private long getExampleKey() {
        return 1500;
    }
}
