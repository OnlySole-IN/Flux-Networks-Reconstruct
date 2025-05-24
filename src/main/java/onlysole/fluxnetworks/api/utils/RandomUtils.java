package onlysole.fluxnetworks.api.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static float nextFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }
}
