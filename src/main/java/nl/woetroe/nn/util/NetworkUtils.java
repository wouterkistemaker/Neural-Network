package nl.woetroe.nn.util;

import java.security.SecureRandom;

public final class NetworkUtils {

    private NetworkUtils() {
    }

    private static final SecureRandom random = new SecureRandom();

    public static double nextDouble() {
        return random.nextDouble();
    }
}
