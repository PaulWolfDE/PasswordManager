package de.paulwolf.passwordmanager.wizards;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;

import static de.paulwolf.passwordmanager.wizards.EncodingWizard.bytesToHex;

public class StringWizard {

    public final static String headBodySeparator = "\n\n\n";
    public final static String separator = "\n";
    public final static String endEntrySeparator = "\n\n";

    public static String makeDatabaseBody(Database database) {

        StringBuilder databaseBody = new StringBuilder();

        ArrayList<Entry> entries = database.getEntries();

        for (int i = 0; i < entries.size(); i++) {

            databaseBody.append(entries.get(i).getTitle().equals("") ? "-" : entries.get(i).getTitle());
            databaseBody.append(separator);
            databaseBody.append(entries.get(i).getUsername().equals("") ? "-" : entries.get(i).getUsername());
            databaseBody.append(separator);
            databaseBody.append(entries.get(i).getEmail().equals("") ? "-" : entries.get(i).getEmail());
            databaseBody.append(separator);
            databaseBody.append(entries.get(i).getPassword().length == 0 ? "-" : new String(entries.get(i).getPassword(), Configuration.STANDARD_CHARSET));
            databaseBody.append(separator);
            databaseBody.append(Configuration.DATE_FORMAT.format(entries.get(i).getLastModified()));
            databaseBody.append(separator);
            databaseBody.append(entries.get(i).getNotes().equals("") ? "-" : entries.get(i).getNotes());
            if (i < entries.size() - 1)
                databaseBody.append(endEntrySeparator);
        }

        return databaseBody.toString();
    }

    public static String makeString(Database database, byte[] iv, byte[] salt)
            throws NoSuchAlgorithmException {

        StringBuilder databaseString = new StringBuilder();
        String databaseBody = makeDatabaseBody(database);

        MessageDigest digest = MessageDigest.getInstance(database.getHashAlgorithm());

        databaseString.append("PasswordManager<" + Configuration.VERSION_NUMBER + '>');
        databaseString.append(separator);
        databaseString.append(bytesToHex(digest.digest(databaseBody.getBytes(Configuration.STANDARD_CHARSET))));
        databaseString.append(separator);
        databaseString.append(database.getEncryptionAlgorithm());
        databaseString.append(separator);
        databaseString.append(database.getHashAlgorithm());
        databaseString.append(separator);
        databaseString.append(bytesToHex(iv));
        databaseString.append(separator);
        databaseString.append(bytesToHex(salt));
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

        if (entryStrings[0].length == 5) {
            for (String[] entryString : entryStrings)
                database.addEntry(new Entry(entryString[0].equals("-") ? "" : entryString[0],
                        entryString[1].equals("-") ? "" : entryString[1],
                        entryString[2].equals("-") ? "" : entryString[2],
                        entryString[3].equals("-") ? "".getBytes(Configuration.STANDARD_CHARSET) : entryString[3].getBytes(Configuration.STANDARD_CHARSET),
                        Configuration.DATE_FORMAT.parse(entryString[4]), ""));
        } else {
            for (String[] entryString : entryStrings)
                database.addEntry(new Entry(entryString[0].equals("-") ? "" : entryString[0],
                        entryString[1].equals("-") ? "" : entryString[1],
                        entryString[2].equals("-") ? "" : entryString[2],
                        entryString[3].equals("-") ? ".".getBytes(Configuration.STANDARD_CHARSET) : entryString[3].getBytes(Configuration.STANDARD_CHARSET),
                        Configuration.DATE_FORMAT.parse(entryString[4]),
                        entryString[5].equals("-") ? "" : entryString[5]));
        }

        ArrayList<Entry> entries = database.getEntries();
        for (Entry e : entries)
            if (e.getTitle().equals(Configuration.BACKUP_TITLE))
                return database;

        database.addEntry(new Entry(Configuration.BACKUP_TITLE, "", "", ".".getBytes(), ""));
        return database;
    }
}
