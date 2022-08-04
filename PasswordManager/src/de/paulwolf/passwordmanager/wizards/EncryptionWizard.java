package de.paulwolf.passwordmanager.wizards;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.WrongPasswordException;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EncryptionWizard {

    public static String encrypt(String databaseString, byte[] key, byte[] iv) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException {

        String[] headBodyStrings = databaseString.split(StringWizard.headBodySeparator);
        String[] headStrings = headBodyStrings[0].split(StringWizard.separator);

        byte[] ciphertext = encrypt(
                headStrings[2],
                headBodyStrings[1].getBytes(Main.STANDARD_CHARSET),
                new SecretKeySpec(key, 0, key.length,
                        headStrings[2].equals("Blowfish/ECB/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CBC/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CTR/NoPadding") ? "Blowfish" : "AES"),
                iv
        );

        return headBodyStrings[0] + StringWizard.headBodySeparator + EncodingWizard.bytesToHex(ciphertext);
    }

    public static String decrypt(String cipherString, byte[] key, byte[] iv) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException, WrongPasswordException {

        String[] headBodyStrings = cipherString.split(StringWizard.headBodySeparator);
        String[] headStrings = headBodyStrings[0].split(StringWizard.separator);

        MessageDigest digest = MessageDigest.getInstance(headStrings[3]);

        byte[] plaintext = decrypt(headStrings[2], EncodingWizard.hexToBytes(headBodyStrings[1]),
                new SecretKeySpec(key, 0, key.length,
                        headStrings[2].equals("Blowfish/ECB/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CBC/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CTR/NoPadding") ? "Blowfish" : "AES"),
                iv);

        byte[] verificationHex;

        if (headStrings[0].charAt(16) == '1') { // Hash over ASCII body
            if (!new String(plaintext, Main.STANDARD_CHARSET).matches("\\A\\p{ASCII}*\\z")) {
                StringBuilder asciiBody = new StringBuilder();
                for (byte b : plaintext)
                    if ((b & 0xFF) >> 7 == 0)
                        asciiBody.append((char) b);
                verificationHex = digest.digest(asciiBody.toString().getBytes(StandardCharsets.US_ASCII));
            } else {
                verificationHex = digest.digest(plaintext);
            }
        } else { // Hash over all body
            verificationHex = digest.digest(plaintext);
        }

        if (Arrays.equals(verificationHex, EncodingWizard.hexToBytes(headStrings[1])))
            return headBodyStrings[0] + StringWizard.headBodySeparator + new String(plaintext, Main.STANDARD_CHARSET);

        throw new WrongPasswordException("The entered password is incorrect!");
    }

    private static byte[] encrypt(String algorithm, byte[] plaintext, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] ciphertext;

        switch (algorithm) {
            case "Twofish/CTR/NoPadding":
                CipherAlgorithm ca = new CipherAlgorithm("Twofish");
                return ca.ctrEncrypt(plaintext, key.getEncoded(), iv);
            case "Twofish/CBC/ISO10126":
                CipherAlgorithm ca1 = new CipherAlgorithm("Twofish");
                return ca1.cbcEncrypt(plaintext, key.getEncoded(), iv);
            case "Twofish/ECB/ISO10126":
                CipherAlgorithm ca2 = new CipherAlgorithm("Twofish");
                return ca2.ecbEncrypt(plaintext, key.getEncoded());
            case "Serpent/CTR/NoPadding":
                CipherAlgorithm ca3 = new CipherAlgorithm("Serpent");
                return ca3.ctrEncrypt(plaintext, key.getEncoded(), iv);
            case "Serpent/CBC/ISO10126":
                CipherAlgorithm ca4 = new CipherAlgorithm("Serpent");
                return ca4.cbcEncrypt(plaintext, key.getEncoded(), iv);
            case "Serpent/ECB/ISO10126":
                CipherAlgorithm ca5 = new CipherAlgorithm("Serpent");
                return ca5.ecbEncrypt(plaintext, key.getEncoded());
            default:
                Cipher cipher = Cipher.getInstance(algorithm);
                switch (algorithm) {
                    case "Blowfish/ECB/PKCS5Padding":
                    case "AES/ECB/PKCS5Padding":
                        cipher.init(1, key);
                        break;
                    case "AES/CBC/PKCS5Padding":
                    case "AES/CTR/NoPadding":
                        cipher.init(1, key, new IvParameterSpec(iv));
                        break;
                    case "AES/GCM/NoPadding":
                        cipher.init(1, key, new GCMParameterSpec(128, iv));
                        break;
                    case "Blowfish/CBC/PKCS5Padding":
                    case "Blowfish/CTR/NoPadding":
                        byte[] bfIV = new byte[8];
                        System.arraycopy(iv, 0, bfIV, 0, bfIV.length);
                        cipher.init(1, key, new IvParameterSpec(bfIV));
                        break;
                }
                ciphertext = cipher.doFinal(plaintext);
        }
        return ciphertext;
    }

    private static byte[] decrypt(String algorithm, byte[] ciphertext, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] plaintext;

        switch (algorithm) {
            case "Twofish/CTR/NoPadding":
                CipherAlgorithm ca = new CipherAlgorithm("Twofish");
                return ca.ctrDecrypt(ciphertext, key.getEncoded(), iv);
            case "Twofish/CBC/ISO10126":
                CipherAlgorithm ca1 = new CipherAlgorithm("Twofish");
                return ca1.cbcDecrypt(ciphertext, key.getEncoded(), iv);
            case "Twofish/ECB/ISO10126":
                CipherAlgorithm ca2 = new CipherAlgorithm("Twofish");
                return ca2.ecbDecrypt(ciphertext, key.getEncoded());
            case "Serpent/CTR/NoPadding":
                CipherAlgorithm ca3 = new CipherAlgorithm("Serpent");
                return ca3.ctrDecrypt(ciphertext, key.getEncoded(), iv);
            case "Serpent/CBC/ISO10126":
                CipherAlgorithm ca4 = new CipherAlgorithm("Serpent");
                return ca4.cbcDecrypt(ciphertext, key.getEncoded(), iv);
            case "Serpent/ECB/ISO10126":
                CipherAlgorithm ca5 = new CipherAlgorithm("Serpent");
                return ca5.ecbDecrypt(ciphertext, key.getEncoded());
            default:
                Cipher cipher = Cipher.getInstance(algorithm);
                switch (algorithm) {
                    case "Blowfish/ECB/PKCS5Padding":
                    case "AES/ECB/PKCS5Padding":
                        cipher.init(2, key);
                        break;
                    case "AES/CBC/PKCS5Padding":
                    case "AES/CTR/NoPadding":
                        cipher.init(2, key, new IvParameterSpec(iv));
                        break;
                    case "AES/GCM/NoPadding":
                        cipher.init(2, key, new GCMParameterSpec(128, iv));
                        break;
                    case "Blowfish/CBC/PKCS5Padding":
                    case "Blowfish/CTR/NoPadding":
                        byte[] bfIV = new byte[8];
                        System.arraycopy(iv, 0, bfIV, 0, bfIV.length);
                        cipher.init(2, key, new IvParameterSpec(bfIV));
                        break;
                }
                plaintext = cipher.doFinal(ciphertext);
        }
        return plaintext;
    }
}
