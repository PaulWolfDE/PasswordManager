package de.paulwolf.passwordmanager.wizards;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.paulwolf.passwordmanager.information.WrongPasswordException;

public class EncryptionWizard {

	public static String encrypt(String databaseString, byte[] key, byte[] iv) throws NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
			IllegalBlockSizeException, UnsupportedEncodingException {

		String[] headBodyStrings = databaseString.split(StringWizard.headBodySeparator);
		String[] headStrings = headBodyStrings[0].split(StringWizard.separator);
		String[] bodyStrings = headBodyStrings[1].split(StringWizard.endEntrySeparator);

		String[][] entryStrings = new String[bodyStrings.length][5];

		for (int i = 0; i < bodyStrings.length; i++)
			entryStrings[i] = bodyStrings[i].split(StringWizard.separator);

		String ciphertext = encrypt(headStrings[2], headBodyStrings[1],
				new SecretKeySpec(key, 0, key.length,
						headStrings[2].equals("Blowfish/ECB/PKCS5Padding")
								|| headStrings[2].equals("Blowfish/CBC/PKCS5Padding")
								|| headStrings[2].equals("Blowfish/CTR/NoPadding") ? "Blowfish" : "AES"),
				iv);

		String cipherFileString = String.valueOf(headBodyStrings[0]) + StringWizard.headBodySeparator + ciphertext;

		return cipherFileString;
	}

	public static String decrypt(String cipherString, byte[] key, byte[] iv) throws NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
			IllegalBlockSizeException, UnsupportedEncodingException, WrongPasswordException {

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
			String asciiBody = "";
			for (int i = 0; i < plaintext.length(); i++)
				if (String.valueOf(plaintext.charAt(i)).matches("\\A\\p{ASCII}*\\z"))
					asciiBody += plaintext.charAt(i);
			verificationHex = bytesToHex(digest.digest(asciiBody.getBytes()));
		} else {
			verificationHex = bytesToHex(digest.digest(plaintext.getBytes()));
		}

		if (verificationHex.equals(headStrings[1]))
			return String.valueOf(headBodyStrings[0]) + StringWizard.headBodySeparator + plaintext;
		
		throw new WrongPasswordException("The entered password is incorrect!");
	}

	public static String bytesToHex(byte[] bytes) {

		StringBuilder hexString = new StringBuilder(2 * bytes.length);

		for (int i = 0; i < bytes.length; i++) {

			String hex = Integer.toHexString(0xFF & bytes[i]);

			if (hex.length() == 1)
				hexString.append('0');

			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static byte[] hexToBytes(String hex) {
		int l = hex.length();
		byte[] data = new byte[l / 2];
		for (int i = 0; i < l; i += 2)
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		return data;
	}

	private static String encrypt(String algorithm, String input, SecretKey key, byte[] iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

		byte[] ciphertext = null;

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
				for (int i = 0; i < bfIV.length; i++)
					bfIV[i] = iv[i];
				cipher.init(1, key, new IvParameterSpec(bfIV));
			}
			ciphertext = cipher.doFinal(input.getBytes());
		}
		return bytesToHex(ciphertext);
	}

	private static String decrypt(String algorithm, String cipherText, SecretKey key, byte[] iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		byte[] plainText = null;

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
				for (int i = 0; i < bfIV.length; i++)
					bfIV[i] = iv[i];
				cipher.init(2, key, new IvParameterSpec(bfIV));
			}
			plainText = cipher.doFinal(hexToBytes(cipherText));
		}

		return new String(plainText);
	}
}
