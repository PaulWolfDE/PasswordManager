package de.paulwolf.passwordmanager.wizards;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.information.WrongPasswordException;
import de.paulwolf.passwordmanager.ui.windows.DatabaseUI;
import de.paulwolf.passwordmanager.utility.JSONParser;
import gnu.crypto.mac.HMacFactory;
import gnu.crypto.mac.IMac;
import gnu.crypto.prng.LimitReachedException;
import gnu.crypto.prng.PBKDF2;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.text.ParseException;
import java.util.*;

import static de.paulwolf.passwordmanager.wizards.EncodingWizard.bytesToHex;
import static de.paulwolf.passwordmanager.wizards.EncodingWizard.hexToBytes;

public class FileWizard {

    public static void saveDatabase(Database db, File file) throws NoSuchAlgorithmException, IOException,
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException, IllegalStateException, LimitReachedException, JSchException {
        FileWriter writer = new FileWriter(file);
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[16], salt = new byte[16];
        sr.nextBytes(iv);
        sr.nextBytes(salt);
        byte[] derivedKey = new byte[32];
        IMac prf = HMacFactory.getInstance(Configuration.HMAC_ALGORITHM);
        PBKDF2 pbkdf2 = new PBKDF2(prf);
        Map<String, Object> map = new HashMap<>();
        map.put("gnu.crypto.pbe.salt", salt);
        map.put("gnu.crypto.pbe.password", new String(db.getMasterKey(), Configuration.STANDARD_CHARSET).toCharArray());
        map.put("gnu.crypto.pbe.iteration.count", Configuration.ITERATIONS);
        pbkdf2.init(map);
        pbkdf2.nextBytes(derivedKey, 0, derivedKey.length);

        writer.write(EncryptionWizard.encrypt(StringWizard.makeString(db, iv, salt), derivedKey, iv));
        writer.close();

        ArrayList<Entry> entries = db.getEntries();
        for (Entry e : entries) {
            if (e.getTitle().equals(Configuration.BACKUP_TITLE)) {
                if (!e.getUsername().equals("") && !e.getEmail().equals("")) {
                    try {
                        BackupWizard.createBackup(e.getUsername(), e.getEmail(), e.getPassword(), db);
                    } catch (SftpException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    public static boolean openDatabase(File file, byte[] key)
            throws FileNotFoundException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            ParseException, IllegalStateException, LimitReachedException, BadPaddingException, WrongPasswordException {
        StringBuilder databaseString = new StringBuilder();
        Scanner scanner = new Scanner(file);
        int i = 0;
        while (scanner.hasNextLine()) {
            if (i != 0)
                databaseString.append(StringWizard.separator);
            i++;
            databaseString.append(scanner.nextLine());
        }
        scanner.close();

        String[] splitDatabase = databaseString.toString().split(StringWizard.separator);
        byte[] iv = new byte[16];
        if (splitDatabase[4].contains(",")) // Version prior 1.4.4
            for (int j = 0; j < iv.length; j++)
                iv[j] = Byte.parseByte(splitDatabase[4].split(",")[j]);
        else // Later versions
            iv = hexToBytes(splitDatabase[4]);


        byte[] derivedKey = new byte[32];
        if (splitDatabase[5].equals("")) {
            // Old database --> password hashed with std algo
            MessageDigest md = MessageDigest.getInstance(splitDatabase[3]);
            derivedKey = md.digest(key);
        } else {
            // New database --> password derived with pbkdf2
            byte[] salt = new byte[16];
            if (splitDatabase[5].contains(",")) // Version prior 1.4.4
                for (int j = 0; j < iv.length; j++)
                    salt[j] = Byte.parseByte(splitDatabase[5].split(",")[j]);
            else // Later version
                salt = hexToBytes(splitDatabase[5]);

            IMac prf = HMacFactory.getInstance(Configuration.HMAC_ALGORITHM);
            PBKDF2 pbkdf2 = new PBKDF2(prf);
            Map<String, Object> map = new HashMap<>();
            map.put("gnu.crypto.pbe.salt", salt);
            map.put("gnu.crypto.pbe.password", new String(key, Configuration.STANDARD_CHARSET).toCharArray());
            map.put("gnu.crypto.pbe.iteration.count", Configuration.ITERATIONS);
            pbkdf2.init(map);
            pbkdf2.nextBytes(derivedKey, 0, derivedKey.length);
        }

        Database database = StringWizard.evaluateString(EncryptionWizard.decrypt(databaseString.toString(), derivedKey, iv));
        database.setPath(file);
        database.setMasterKey(new SecretKeySpec(key, splitDatabase[2].contains("Blowfish") ? "Blowfish" :"AES"));
        new DatabaseUI(database, null);
        return true;
    }

    // Checks whether the database was modified.
    public static boolean isDatabaseModified(Database db, File file) throws NoSuchAlgorithmException {

        StringBuilder databaseString = new StringBuilder();
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return true;
        }
        int i = 0;
        while (scanner.hasNextLine()) {
            if (i != 0)
                databaseString.append(StringWizard.separator);
            i++;
            databaseString.append(scanner.nextLine());
        }
        scanner.close();

        String[] splitDatabase = databaseString.toString().split(StringWizard.separator);
        MessageDigest digest = MessageDigest.getInstance(db.getHashAlgorithm());
        String databaseBody = StringWizard.makeDatabaseBody(db);
        return !splitDatabase[1].equals(bytesToHex(digest.digest(databaseBody.getBytes(Configuration.STANDARD_CHARSET))));
    }

    public static boolean isCompatible(File f) throws IOException {

        Scanner scanner = new Scanner(f);
        // Signature is found on the first line of the file
        String signature = scanner.nextLine();

        String version = signature.substring(signature.indexOf("<") + 1);
        version = version.substring(0, version.indexOf(">"));

        for (int i = 0; i < Configuration.COMPATIBLE_VERSIONS.length; i++) {
            if (version.equals(Configuration.COMPATIBLE_VERSIONS[i]))
                return true;
        }
        return JSONParser.checkRemoteCompatibility(version);
    }

    public static String[] getRecentlyOpenedFiles(boolean onlyCompatibles) throws IOException {

        ArrayList<String> filesCompatible = new ArrayList<>();
        ArrayList<String> filesToKeep = new ArrayList<>();

        File recentlyOpened;
        if (System.getenv("Appdata") == null)
            recentlyOpened = new File(System.getProperty("user.home") + "/PasswordManager/.pmrc"); // Linux
        else
            recentlyOpened = new File(System.getenv("Appdata") + "/PasswordManager/.pmrc"); // Windows

        Scanner scanner;
        try {
            scanner = new Scanner(recentlyOpened);
        } catch (FileNotFoundException e) {
            return null;
        }
        while (scanner.hasNextLine()) {
            File currentFile = new File(scanner.nextLine());
            if (Files.exists(currentFile.toPath()) && isCompatible(currentFile)) {
                filesCompatible.add(currentFile.getCanonicalPath());
                filesToKeep.add(currentFile.getCanonicalPath());
            } else if (!Files.exists(currentFile.toPath())) {
                filesToKeep.add(currentFile.getCanonicalPath());
            }
        }
        scanner.close();

        if (onlyCompatibles)
            return filesCompatible.toArray(new String[]{});
        return filesToKeep.toArray(new String[]{});
    }

    public static void updateRecentlyOpened(String fileOpened) throws IOException {

        File recentlyOpened;
        if (System.getenv("Appdata") == null)
            recentlyOpened = new File(System.getProperty("user.home") + "/PasswordManager/.pmrc"); // Linux
        else
            recentlyOpened = new File(System.getenv("Appdata") + "/PasswordManager/.pmrc"); // Windows

        // Directory hierarchy for rc file gets created
        (new File(recentlyOpened.getCanonicalPath() + "/../)")).mkdirs();

        String[] filesToKeep = getRecentlyOpenedFiles(false);

        recentlyOpened.createNewFile();
        FileWriter writer = new FileWriter(recentlyOpened);
        writer.write(fileOpened + "\n");
        if (filesToKeep == null)
            return;
        for (String file : filesToKeep) {
            if (!new File(file).getCanonicalPath().equals(fileOpened))
                writer.write(file + "\n");
        }
        writer.close();
    }
}
