package de.paulwolf.passwordmanager.wizards;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;

public class StringWizard {

	public final static String headBodySeparator = "\n\n\n";
	public final static String separator = "\n";
	public final static String endEntrySeparator = "\n\n";

	public static String makeString(Database database, byte[] iv, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		String databaseString = "";
		StringBuilder databaseBody = new StringBuilder();

		ArrayList<Entry> entries = database.getEntries();

		for (int i = 0; i < entries.size(); i++) {

			databaseBody.append(entries.get(i).getTitle().equals("") ? "-" : entries.get(i).getTitle());
			databaseBody.append(separator);
			databaseBody.append(entries.get(i).getUsername().equals("") ? "-" : entries.get(i).getUsername());
			databaseBody.append(separator);
			databaseBody.append(entries.get(i).getEmail().equals("") ? "-" : entries.get(i).getEmail());
			databaseBody.append(separator);
			databaseBody.append(entries.get(i).getPassword().equals("") ? "-" : entries.get(i).getPassword());
			databaseBody.append(separator);
			databaseBody.append(Main.DATE_FORMAT.format(entries.get(i).getLastModified()));
			databaseBody.append(separator);
			databaseBody.append(entries.get(i).getNotes().equals("") ? "-" : entries.get(i).getNotes());
			if (i < entries.size() - 1)
				databaseBody.append(endEntrySeparator);
		}

		MessageDigest digest = MessageDigest.getInstance(database.getHashAlgorithm());

		databaseString += "PasswordManager<" + Main.VERSION_NUMBER + '>';
		databaseString += separator;

		if (!databaseBody.toString().matches("\\A\\p{ASCII}*\\z")) {
			String asciiBody = "";
			for (int i = 0; i < databaseBody.length(); i++)
				if (String.valueOf(databaseBody.charAt(i)).matches("\\A\\p{ASCII}*\\z"))
					asciiBody += databaseBody.charAt(i);
			databaseString += EncryptionWizard.bytesToHex(digest.digest(asciiBody.getBytes()));
		} else {
			databaseString += EncryptionWizard.bytesToHex(digest.digest(databaseBody.toString().getBytes()));
		}

		databaseString += separator;
		databaseString += database.getEncryptionAlgorithm();
		databaseString += separator;
		databaseString += database.getHashAlgorithm();
		databaseString += separator;
		for (int i = 0; i < iv.length; i++) {
			databaseString += iv[i];
			if (i != 15)
				databaseString += ",";
		}
		databaseString += separator;
		for (int i = 0; i < salt.length; i++) {
			databaseString += salt[i];
			if (i != 15)
				databaseString += ",";
		}
		databaseString += headBodySeparator;
		databaseString += databaseBody;
		return databaseString;
	}

	public static Database evaluateString(String databaseString) throws ParseException, UnsupportedEncodingException {

		Database database = new Database();

		String[] headBodyStrings = databaseString.split(headBodySeparator);
		String[] headStrings = headBodyStrings[0].split(separator);
		String[] bodyStrings = headBodyStrings[1].split(endEntrySeparator); // => 1 array index = 1 entry
		String[][] entryStrings = new String[bodyStrings.length][6];

		for (int i = 0; i < bodyStrings.length; i++)
			entryStrings[i] = bodyStrings[i].split(separator);

		database.setHashAlgorithm(headStrings[3]);
		database.setEncryptionAlgorithm(headStrings[2]);
		database.setMasterKey(headStrings[0].getBytes());

		if (entryStrings[0].length == 5) {
			for (int i = 0; i < entryStrings.length; i++)
				database.addEntry(new Entry(entryStrings[i][0].equals("-") ? "" : entryStrings[i][0],
						entryStrings[i][1].equals("-") ? "" : entryStrings[i][1],
						entryStrings[i][2].equals("-") ? "" : entryStrings[i][2],
						entryStrings[i][3].equals("-") ? "" : entryStrings[i][3],
						Main.DATE_FORMAT.parse(entryStrings[i][4]), ""));
		} else {
			for (int i = 0; i < entryStrings.length; i++)
				database.addEntry(new Entry(entryStrings[i][0].equals("-") ? "" : entryStrings[i][0],
						entryStrings[i][1].equals("-") ? "" : entryStrings[i][1],
						entryStrings[i][2].equals("-") ? "" : entryStrings[i][2],
						entryStrings[i][3].equals("-") ? "" : entryStrings[i][3],
						Main.DATE_FORMAT.parse(entryStrings[i][4]),
						entryStrings[i][5].equals("-") ? "" : entryStrings[i][5]));
		}

		return database;
	}
}
