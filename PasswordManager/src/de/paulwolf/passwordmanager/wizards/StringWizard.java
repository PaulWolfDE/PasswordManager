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

	public static String makeString(Database database, byte[] iv)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		String databaseString = "";
		String databaseBody = "";

		ArrayList<Entry> entries = database.getEntries();

		for (int i = 0; i < entries.size(); i++) {

			databaseBody += entries.get(i).getTitle();
			databaseBody += separator;
			databaseBody += entries.get(i).getUsername();
			databaseBody += separator;
			databaseBody += entries.get(i).getEmail();
			databaseBody += separator;
			databaseBody += entries.get(i).getPassword();
			databaseBody += separator;
			databaseBody += Main.DATE_FORMAT.format(entries.get(i).getLastModified());
			databaseBody += separator;
			databaseBody += entries.get(i).getNotes().equals("") ? "-" : entries.get(i).getNotes();
			if (i < entries.size() - 1)
				databaseBody += endEntrySeparator;
		}

		MessageDigest digest = MessageDigest.getInstance(database.getHashAlgorithm());

		databaseString += "PasswordManager<" + Main.VERSION_NUMBER + '>';
		databaseString += separator;

		if (!databaseBody.matches("\\A\\p{ASCII}*\\z")) {
			String asciiBody = "";
			for (int i = 0; i < databaseBody.length(); i++)
				if (String.valueOf(databaseBody.charAt(i)).matches("\\A\\p{ASCII}*\\z"))
					asciiBody += databaseBody.charAt(i);
			databaseString += EncryptionWizard.bytesToHex(digest.digest(asciiBody.getBytes()));
		} else {
			databaseString += EncryptionWizard.bytesToHex(digest.digest(databaseBody.getBytes()));
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
				database.addEntry(new Entry(entryStrings[i][0], entryStrings[i][1], entryStrings[i][2],
						entryStrings[i][3], Main.DATE_FORMAT.parse(entryStrings[i][4]), ""));
		} else {
			for (int i = 0; i < entryStrings.length; i++)
				database.addEntry(new Entry(entryStrings[i][0], entryStrings[i][1], entryStrings[i][2],
						entryStrings[i][3], Main.DATE_FORMAT.parse(entryStrings[i][4]), entryStrings[i][5]));
		}

		return database;
	}
}
