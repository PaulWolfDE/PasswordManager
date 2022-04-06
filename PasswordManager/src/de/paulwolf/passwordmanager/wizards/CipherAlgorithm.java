package de.paulwolf.passwordmanager.wizards;

import gnu.crypto.cipher.BaseCipher;
import gnu.crypto.cipher.CipherFactory;

import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Arrays;

public class CipherAlgorithm {

    private static final int BLOCK_SIZE = 16;

    private final BaseCipher cipher;

    public CipherAlgorithm(String algorithm) {
        cipher = (BaseCipher) CipherFactory.getInstance(algorithm);
    }

    static byte[] pad(byte[] in) {
        byte[] ret = new byte[in.length + BLOCK_SIZE - in.length % BLOCK_SIZE];
        System.arraycopy(in, 0, ret, 0, in.length);
        byte paddedBytes = 0;
        SecureRandom sr = new SecureRandom();
        for (int i = in.length; i < ret.length - 1; i++) {
            ret[i] = (byte) sr.nextInt(256);
            paddedBytes++;
        }
        ret[ret.length - 1] = paddedBytes;
        return ret;
    }

    public static byte[] unpad(byte[] in) {
        byte[] ret = new byte[in.length - in[in.length - 1] - 1];
        if (ret.length > in.length)
            return in;
        System.arraycopy(in, 0, ret, 0, ret.length);
        return ret;
    }

    public byte[] ecbEncrypt(byte[] plaintext, byte[] key) throws InvalidKeyException {
        Object sKey = cipher.makeKey(key, BLOCK_SIZE);
        byte[] pt = pad(plaintext);
        byte[] ciphertext = new byte[pt.length];
        for (int i = 0; i < pt.length; i += BLOCK_SIZE)
            cipher.encrypt(pt, i, ciphertext, i, sKey, BLOCK_SIZE);
        return ciphertext;
    }

    public byte[] ecbDecrypt(byte[] ciphertext, byte[] key) throws InvalidKeyException {
        Object sKey = cipher.makeKey(key, BLOCK_SIZE);
        byte[] plaintext = new byte[ciphertext.length];
        for (int i = 0; i < ciphertext.length; i += BLOCK_SIZE)
            cipher.decrypt(ciphertext, i, plaintext, i, sKey, BLOCK_SIZE);
        return unpad(plaintext);
    }

    public byte[] cbcEncrypt(byte[] plaintext, byte[] key, byte[] iv) throws InvalidKeyException {
        Object sKey = cipher.makeKey(key, BLOCK_SIZE);
        byte[] pt = pad(plaintext);
        byte[] ciphertext = new byte[pt.length];
        for (int i = 0; i < BLOCK_SIZE; i++)
            pt[i] ^= iv[i];
        cipher.encrypt(pt, 0, ciphertext, 0, sKey, BLOCK_SIZE);
        for (int i = BLOCK_SIZE; i < pt.length; i += BLOCK_SIZE) {
            for (int j = 0; j < BLOCK_SIZE; j++)
                pt[i + j] ^= ciphertext[i + j - BLOCK_SIZE];
            cipher.encrypt(pt, i, ciphertext, i, sKey, BLOCK_SIZE);
        }
        return ciphertext;
    }

    public byte[] cbcDecrypt(byte[] ciphertext, byte[] key, byte[] iv) throws InvalidKeyException {
        Object sKey = cipher.makeKey(key, BLOCK_SIZE);
        byte[] plaintext = new byte[ciphertext.length];
        cipher.decrypt(ciphertext, 0, plaintext, 0, sKey, BLOCK_SIZE);
        for (int i = 0; i < BLOCK_SIZE; i++)
            plaintext[i] ^= iv[i];
        for (int i = BLOCK_SIZE; i < ciphertext.length; i += BLOCK_SIZE) {
            cipher.decrypt(ciphertext, i, plaintext, i, sKey, BLOCK_SIZE);
            for (int j = 0; j < BLOCK_SIZE; j++)
                plaintext[i + j] ^= ciphertext[i + j - BLOCK_SIZE];
        }
        return unpad(plaintext);
    }

    public byte[] ctrEncrypt(byte[] plaintext, byte[] key, byte[] iv) throws InvalidKeyException {
        Object sKey = cipher.makeKey(key, BLOCK_SIZE);
        byte[] initializationVector = Arrays.copyOf(iv, iv.length);
        byte[] counter = {-128, -128, -128, -128, -128, -128, -128, -128};
        byte[] ciphertext = new byte[plaintext.length];
        for (int i = 0; i < plaintext.length / BLOCK_SIZE + 1; i++) {
            for (int j = 0; j < counter.length; j++) {
                if (counter[j] == 127 && j != 7) {
                    counter[j] = -128;
                    counter[j + 1]++;
                }
                if (counter[j] != -128)
                    initializationVector[BLOCK_SIZE - 1 - j] = counter[j];
                else
                    initializationVector[BLOCK_SIZE - 1 - j] = iv[BLOCK_SIZE - 1 - j];
            }
            byte[] temp = new byte[BLOCK_SIZE];
            cipher.encrypt(initializationVector, 0, temp, 0, sKey, BLOCK_SIZE);
            for (int j = 0; i < plaintext.length / BLOCK_SIZE ? j < BLOCK_SIZE : j < plaintext.length % BLOCK_SIZE; j++)
                ciphertext[i * BLOCK_SIZE + j] = (byte) (plaintext[i * BLOCK_SIZE + j] ^ temp[j]);
            counter[0]++;
        }
        return ciphertext;
    }

    public byte[] ctrDecrypt(byte[] ciphertext, byte[] key, byte[] iv) throws InvalidKeyException {
        return ctrEncrypt(ciphertext, key, iv);
    }
}
