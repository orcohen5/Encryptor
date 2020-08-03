package main.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class KeyGenerator {
    private static final int MAX_KEY_VALUE = 3000;

    @Autowired
    public KeyGenerator() {

    }

    public long generateKey() {
        return ThreadLocalRandom.current().nextInt(1, MAX_KEY_VALUE);
    }

    public List<Long> generateKeyListByRepetitionsNumber(int repetitionsNumber) {
        List<Long> keyList = new ArrayList();

        for(int i = 0; i < repetitionsNumber; i++) {
            keyList.add(generateKey());
        }

        return keyList;
    }
}
