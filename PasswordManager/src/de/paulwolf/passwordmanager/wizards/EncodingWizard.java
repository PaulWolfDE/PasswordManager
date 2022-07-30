package de.paulwolf.passwordmanager.wizards;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class EncodingWizard {

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

    public static byte[] charsToBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.US_ASCII.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }

    public static String decodeString(int encoding, String str) {
        if (encoding == 0)
            return str;
        else if (encoding == 1)
            return new String(hexToBytes(str));
        else
            return new String(Objects.requireNonNull(base64ToBytes(str.getBytes())));
    }

    public static boolean isEncodingValid(int encoding, String str) {

        if (encoding == 0) {
            return str.matches("\\A\\p{ASCII}*\\z");
        } else if (encoding == 1) {
            return str.matches("-?[0-9a-fA-F]+") && str.length() % 2 == 0;
        } else {
            return base64ToBytes(str.getBytes()) != null;
        }
    }
}
