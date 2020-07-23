package entities;

import java.util.concurrent.ThreadLocalRandom;

public class KeyGenerator {
    private static final int MAX_KEY_VALUE = 3000;

    public long generateKey() {
        return ThreadLocalRandom.current().nextInt(1, MAX_KEY_VALUE);
    }
}
