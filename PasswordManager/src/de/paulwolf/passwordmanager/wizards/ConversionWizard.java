package de.paulwolf.passwordmanager.wizards;

import java.util.Base64;

public class ConversionWizard {

    public static byte[] hexToBytes(String hexString) {
        int length = hexString.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2)
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        return bytes;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] bytesToBase64(byte[] bytes) {
        try {
            return Base64.getEncoder().encode(bytes);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static byte[] base64ToBytes(byte[] base64) {
        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
