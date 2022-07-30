package de.paulwolf.passwordmanager.wizards;

import de.paulwolf.passwordmanager.information.WrongPasswordException;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static de.paulwolf.passwordmanager.wizards.EncodingWizard.bytesToHex;
import static de.paulwolf.passwordmanager.wizards.EncodingWizard.hexToBytes;

public class EncryptionWizard {

    public static String encrypt(String databaseString, byte[] key, byte[] iv) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException {

        String[] headBodyStrings = databaseString.split(StringWizard.headBodySeparator);
        String[] headStrings = headBodyStrings[0].split(StringWizard.separator);

        String ciphertext = encrypt(headStrings[2], headBodyStrings[1],
                new SecretKeySpec(key, 0, key.length,
                        headStrings[2].equals("Blowfish/ECB/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CBC/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CTR/NoPadding") ? "Blowfish" : "AES"),
                iv);

        return headBodyStrings[0] + StringWizard.headBodySeparator + ciphertext;
    }

    public static String decrypt(String cipherString, byte[] key, byte[] iv) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException, WrongPasswordException {

        String[] headBodyStrings = cipherString.split(StringWizard.headBodySeparator);
        String[] headStrings = headBodyStrings[0].split(StringWizard.separator);

        MessageDigest digest = MessageDigest.getInstance(headStrings[3]);

        String plaintext = decrypt(headStrings[2], headBodyStrings[1],
                new SecretKeySpec(key, 0, key.length,
                        headStrings[2].equals("Blowfish/ECB/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CBC/PKCS5Padding")
                                || headStrings[2].equals("Blowfish/CTR/NoPadding") ? "Blowfish" : "AES"),
                iv);

        String verificationHex;

        if (!plaintext.matches("\\A\\p{ASCII}*\\z")) {
            StringBuilder asciiBody = new StringBuilder();
            for (int i = 0; i < plaintext.length(); i++)
                if (String.valueOf(plaintext.charAt(i)).matches("\\A\\p{ASCII}*\\z"))
                    asciiBody.append(plaintext.charAt(i));
            verificationHex = bytesToHex(digest.digest(asciiBody.toString().getBytes()));
        } else {
            verificationHex = bytesToHex(digest.digest(plaintext.getBytes()));
        }

        if (verificationHex.equals(headStrings[1]))
            return headBodyStrings[0] + StringWizard.headBodySeparator + plaintext;

        throw new WrongPasswordException("The entered password is incorrect!");
    }

    private static String encrypt(String algorithm, String input, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] ciphertext;

        switch (algorithm) {
            case "Twofish/CTR/NoPadding":
                CipherAlgorithm ca = new CipherAlgorithm("Twofish");
                return bytesToHex(ca.ctrEncrypt(input.getBytes(), key.getEncoded(), iv));
            case "Twofish/CBC/ISO10126":
                CipherAlgorithm ca1 = new CipherAlgorithm("Twofish");
                return bytesToHex(ca1.cbcEncrypt(input.getBytes(), key.getEncoded(), iv));
            case "Twofish/ECB/ISO10126":
                CipherAlgorithm ca2 = new CipherAlgorithm("Twofish");
                return bytesToHex(ca2.ecbEncrypt(input.getBytes(), key.getEncoded()));
            case "Serpent/CTR/NoPadding":
                CipherAlgorithm ca3 = new CipherAlgorithm("Serpent");
                return bytesToHex(ca3.ctrEncrypt(input.getBytes(), key.getEncoded(), iv));
            case "Serpent/CBC/ISO10126":
                CipherAlgorithm ca4 = new CipherAlgorithm("Serpent");
                return bytesToHex(ca4.cbcEncrypt(input.getBytes(), key.getEncoded(), iv));
            case "Serpent/ECB/ISO10126":
                CipherAlgorithm ca5 = new CipherAlgorithm("Serpent");
                return bytesToHex(ca5.ecbEncrypt(input.getBytes(), key.getEncoded()));
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
                ciphertext = cipher.doFinal(input.getBytes());
        }
        return bytesToHex(ciphertext);
    }

    private static String decrypt(String algorithm, String cipherText, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] plainText;

        switch (algorithm) {
            case "Twofish/CTR/NoPadding":
                CipherAlgorithm ca = new CipherAlgorithm("Twofish");
                return new String(ca.ctrDecrypt(hexToBytes(cipherText), key.getEncoded(), iv));
            case "Twofish/CBC/ISO10126":
                CipherAlgorithm ca1 = new CipherAlgorithm("Twofish");
                return new String(ca1.cbcDecrypt(hexToBytes(cipherText), key.getEncoded(), iv));
            case "Twofish/ECB/ISO10126":
                CipherAlgorithm ca2 = new CipherAlgorithm("Twofish");
                return new String(ca2.ecbDecrypt(hexToBytes(cipherText), key.getEncoded()));
            case "Serpent/CTR/NoPadding":
                CipherAlgorithm ca3 = new CipherAlgorithm("Serpent");
                return new String(ca3.ctrDecrypt(hexToBytes(cipherText), key.getEncoded(), iv));
            case "Serpent/CBC/ISO10126":
                CipherAlgorithm ca4 = new CipherAlgorithm("Serpent");
                return new String(ca4.cbcDecrypt(hexToBytes(cipherText), key.getEncoded(), iv));
            case "Serpent/ECB/ISO10126":
                CipherAlgorithm ca5 = new CipherAlgorithm("Serpent");
                return new String(ca5.ecbDecrypt(hexToBytes(cipherText), key.getEncoded()));
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
                plainText = cipher.doFinal(hexToBytes(cipherText));
        }

        return new String(plainText);
    }
}
