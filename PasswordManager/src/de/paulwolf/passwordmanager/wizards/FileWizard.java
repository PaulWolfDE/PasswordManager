package de.paulwolf.passwordmanager.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.WrongPasswordException;
import de.paulwolf.passwordmanager.ui.DatabaseUI;

public class FileWizard {

	public static void saveDatabase(Database db, File file)
			throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		FileWriter writer = new FileWriter(file);
		SecureRandom sr = new SecureRandom();
		byte[] iv = new byte[16];
		sr.nextBytes(iv);
		writer.write(EncryptionWizard.encrypt(StringWizard.makeString(db, iv), db.getMasterKey(), iv));
		writer.close();
	}

	public static boolean openDatabase(File file, byte[] key) throws FileNotFoundException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
			IllegalBlockSizeException, UnsupportedEncodingException, ParseException {
		String databaseString = "";
		Scanner scanner = new Scanner(file);
		int i = 0;
		while (scanner.hasNextLine()) {
			if (i != 0)
				databaseString = String.valueOf(databaseString) + "\n";
			i++;
			databaseString = String.valueOf(databaseString) + scanner.nextLine();
		}
		scanner.close();

		String ver = databaseString.substring(0, Math.min(databaseString.length(), 21));
		boolean old = false;

		if (ver.equals("PasswordManager<1.3.3") || ver.equals("PasswordManager<1.3.2"))
			old = true;

		String[] splitbase = databaseString.split(old ? StringWizard.oldSeparator
				: (StringWizard.separator + StringWizard.separator));
		if (!isCompatible(splitbase[0])) {
			JOptionPane.showMessageDialog(null,
					"Either the file is not an database file or the version is not compatible!", "Invalid file", 0);
			return false;
		}
		byte[] iv = new byte[16];
		for (int j = 0; j < iv.length; j++)
			iv[j] = Byte.parseByte(splitbase[4].split(",")[j]);

		try {
			Database dtb = StringWizard.evaluateString(EncryptionWizard.decrypt(databaseString, key, iv));
			dtb.setPath(file);
			dtb.setMasterKey(key);
			DatabaseUI.initUI(dtb);
		} catch (WrongPasswordException | BadPaddingException e) {
			JOptionPane.showMessageDialog(null, "The entered password is incorrect!", "Insufficient credentials", 0);
			return false;
		}
		return true;
	}

	private static boolean isCompatible(String version) {
		for (int i = 0; i < Main.COMPATIBLE_VERSIONS.length; i++) {
			String temp = "PasswordManager<" + Main.COMPATIBLE_VERSIONS[i] + ">";
			if (version.substring(0, temp.length()).equals(temp))
				return true;
		}
		return false;
	}
}
