package de.paulwolf.passwordmanager.wizards;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordWizard {

    public static byte[] generatePassword(int length, byte[] charset) {

        SecureRandom random = null;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException ignored) {}
        final byte[] password = new byte[length];

        for (int i = 0; i < length; i++) {
            assert random != null;
            password[i] = charset[random.nextInt(charset.length)];
        }
        return password;
    }

}
