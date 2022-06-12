package de.paulwolf.passwordmanager.wizards;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.WrongPasswordException;
import de.paulwolf.passwordmanager.ui.DatabaseUI;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileWizard {

    public static void saveDatabase(Database db, File file) throws NoSuchAlgorithmException, IOException,
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException, IllegalStateException, LimitReachedException {
        FileWriter writer = new FileWriter(file);
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[16], salt = new byte[16];
        sr.nextBytes(iv);
        sr.nextBytes(salt);
        byte[] derivatedKey = new byte[32];
        IMac prf = HMacFactory.getInstance(Main.HMAC_ALGORITHM);
        PBKDF2 pbkdf2 = new PBKDF2(prf);
        Map<String, Object> map = new HashMap<>();
        map.put("gnu.crypto.pbe.salt", salt);
        map.put("gnu.crypto.pbe.password", new String(db.getMasterKey()).toCharArray());
        map.put("gnu.crypto.pbe.iteration.count", Main.ITERATIONS);
        pbkdf2.init(map);
        pbkdf2.nextBytes(derivatedKey, 0, derivatedKey.length);

        writer.write(EncryptionWizard.encrypt(StringWizard.makeString(db, iv, salt), derivatedKey, iv));
        writer.close();
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
        for (int j = 0; j < iv.length; j++)
            iv[j] = Byte.parseByte(splitbase[4].split(",")[j]);

        byte[] derivatedKey = new byte[32];
        if (splitbase[5].equals("")) {
            // Old database --> password hashed with std algo
            MessageDigest md = MessageDigest.getInstance(splitbase[3]);
            derivatedKey = md.digest(key);
        } else {
            // New database --> password derivated with pbkdf2
            byte[] salt = new byte[16];
            for (int j = 0; j < iv.length; j++)
                salt[j] = Byte.parseByte(splitbase[5].split(",")[j]);
            IMac prf = HMacFactory.getInstance(Main.HMAC_ALGORITHM);
            PBKDF2 pbkdf2 = new PBKDF2(prf);
            Map<String, Object> map = new HashMap<>();
            map.put("gnu.crypto.pbe.salt", salt);
            map.put("gnu.crypto.pbe.password", new String(key).toCharArray());
            map.put("gnu.crypto.pbe.iteration.count", Main.ITERATIONS);
            pbkdf2.init(map);
            pbkdf2.nextBytes(derivatedKey, 0, derivatedKey.length);
        }

        Database dtb = StringWizard.evaluateString(EncryptionWizard.decrypt(databaseString.toString(), derivatedKey, iv));
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
