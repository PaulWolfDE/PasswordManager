package de.paulwolf.passwordmanager.wizards;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import de.paulwolf.passwordmanager.Main;
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
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static de.paulwolf.passwordmanager.wizards.EncodingWizard.hexToBytes;

public class FileWizard {

    public static void saveDatabase(Database db, File file) throws NoSuchAlgorithmException, IOException,
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException, IllegalStateException, LimitReachedException {
        FileWriter writer = new FileWriter(file);
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[16], salt = new byte[16];
        sr.nextBytes(iv);
        sr.nextBytes(salt);
        byte[] derivedKey = new byte[32];
        IMac prf = HMacFactory.getInstance(Main.HMAC_ALGORITHM);
        PBKDF2 pbkdf2 = new PBKDF2(prf);
        Map<String, Object> map = new HashMap<>();
        map.put("gnu.crypto.pbe.salt", salt);
        map.put("gnu.crypto.pbe.password", new String(db.getMasterKey()).toCharArray());
        map.put("gnu.crypto.pbe.iteration.count", Main.ITERATIONS);
        pbkdf2.init(map);
        pbkdf2.nextBytes(derivedKey, 0, derivedKey.length);

        writer.write(EncryptionWizard.encrypt(StringWizard.makeString(db, iv, salt), derivedKey, iv));
        writer.close();

        ArrayList<Entry> entries = db.getEntries();
        for (Entry e : entries) {
            if (e.getTitle().equals(Main.BACKUP_TITLE)) {
                if (!e.getUsername().equals("") && !e.getEmail().equals("")) {
                    try {
                        BackupWizard.createBackup(e.getUsername(), e.getEmail(), e.getPassword().getBytes(), db);
                    } catch (SftpException ex) {
                        System.out.println("SftpException");
                        throw new RuntimeException(ex);
                    } catch (JSchException ex) {
                        System.out.println("JSchException - Connection unavailable");
                        // throw new RuntimeException(ex);
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

        String[] splitbase = databaseString.toString().split(StringWizard.separator);
        if (!isCompatible(splitbase[0])) {
            JOptionPane.showMessageDialog(null,
                    "Either the file is not an database file or the version is not compatible!", "Invalid file", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        byte[] iv = new byte[16];
        if (splitbase[4].contains(",")) // Version prior 1.4.4
            for (int j = 0; j < iv.length; j++)
                iv[j] = Byte.parseByte(splitbase[4].split(",")[j]);
        else // Later versions
            iv = hexToBytes(splitbase[4]);


        byte[] derivedKey = new byte[32];
        if (splitbase[5].equals("")) {
            // Old database --> password hashed with std algo
            MessageDigest md = MessageDigest.getInstance(splitbase[3]);
            derivedKey = md.digest(key);
        } else {
            // New database --> password derived with pbkdf2
            byte[] salt = new byte[16];
            if (splitbase[5].contains(",")) // Version prior 1.4.4
                for (int j = 0; j < iv.length; j++)
                    salt[j] = Byte.parseByte(splitbase[5].split(",")[j]);
            else // Later version
                salt = hexToBytes(splitbase[5]);

            IMac prf = HMacFactory.getInstance(Main.HMAC_ALGORITHM);
            PBKDF2 pbkdf2 = new PBKDF2(prf);
            Map<String, Object> map = new HashMap<>();
            map.put("gnu.crypto.pbe.salt", salt);
            map.put("gnu.crypto.pbe.password", new String(key).toCharArray());
            map.put("gnu.crypto.pbe.iteration.count", Main.ITERATIONS);
            pbkdf2.init(map);
            pbkdf2.nextBytes(derivedKey, 0, derivedKey.length);
        }

        Database dtb = StringWizard.evaluateString(EncryptionWizard.decrypt(databaseString.toString(), derivedKey, iv));
        dtb.setPath(file);
        dtb.setMasterKey(key);
        new DatabaseUI(dtb);
        return true;
    }

    public static boolean isCompatible(String signature) {

        String version = signature.substring(signature.indexOf("<") + 1);
        version = version.substring(0, version.indexOf(">"));

        for (int i = 0; i < Main.COMPATIBLE_VERSIONS.length; i++) {
            if (version.equals(Main.COMPATIBLE_VERSIONS[i]))
                return true;
        }
        return JSONParser.checkRemoteCompatibility(version);
    }
}
