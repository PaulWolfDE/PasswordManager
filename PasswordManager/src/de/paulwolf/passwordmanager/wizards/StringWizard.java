package de.paulwolf.passwordmanager.wizards;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class StringWizard {

    public final static String headBodySeparator = "\n\n\n";
    public final static String separator = "\n";
    public final static String endEntrySeparator = "\n\n";

    public static String makeString(Database database, byte[] iv, byte[] salt)
            throws NoSuchAlgorithmException {

        StringBuilder databaseString = new StringBuilder();
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

        databaseString.append("PasswordManager<" + Main.VERSION_NUMBER + '>');
        databaseString.append(separator);

        if (!databaseBody.toString().matches("\\A\\p{ASCII}*\\z")) {
            StringBuilder asciiBody = new StringBuilder();
            for (int i = 0; i < databaseBody.length(); i++)
                if (String.valueOf(databaseBody.charAt(i)).matches("\\A\\p{ASCII}*\\z"))
                    asciiBody.append(databaseBody.charAt(i));
            databaseString.append(EncryptionWizard.bytesToHex(digest.digest(asciiBody.toString().getBytes())));
        } else {
            databaseString.append(EncryptionWizard.bytesToHex(digest.digest(databaseBody.toString().getBytes())));
        }

        databaseString.append(separator);
        databaseString.append(database.getEncryptionAlgorithm());
        databaseString.append(separator);
        databaseString.append(database.getHashAlgorithm());
        databaseString.append(separator);
        databaseString.append(EncryptionWizard.bytesToHex(iv));
        databaseString.append(separator);
        databaseString.append(EncryptionWizard.bytesToHex(salt));
        databaseString.append(headBodySeparator);
        databaseString.append(databaseBody);
        return databaseString.toString();
    }

    public static Database evaluateString(String databaseString) throws ParseException {

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
            for (String[] entryString : entryStrings)
                database.addEntry(new Entry(entryString[0].equals("-") ? "" : entryString[0],
                        entryString[1].equals("-") ? "" : entryString[1],
                        entryString[2].equals("-") ? "" : entryString[2],
                        entryString[3].equals("-") ? "" : entryString[3],
                        Main.DATE_FORMAT.parse(entryString[4]), ""));
        } else {
            for (String[] entryString : entryStrings)
                database.addEntry(new Entry(entryString[0].equals("-") ? "" : entryString[0],
                        entryString[1].equals("-") ? "" : entryString[1],
                        entryString[2].equals("-") ? "" : entryString[2],
                        entryString[3].equals("-") ? "" : entryString[3],
                        Main.DATE_FORMAT.parse(entryString[4]),
                        entryString[5].equals("-") ? "" : entryString[5]));
        }

        ArrayList<Entry> entries = database.getEntries();
        for (Entry e : entries)
            if (e.getTitle().equals(Main.BACKUP_TITLE))
                return database;

        database.addEntry(new Entry(Main.BACKUP_TITLE, "", "", ".", ""));
        return database;
    }
}
