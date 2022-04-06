package de.paulwolf.passwordmanager;

import de.paulwolf.passwordmanager.ui.MainUI;
import de.paulwolf.passwordmanager.ui.OpenDatabaseUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Main {

    // STANDARD HASH ALGORITHM
    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String[] HASH_ALGORITHMS = {"SHA-256", "MD5"};
    // STANDARD ENCRYPTION ALGORITHM
    public static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    public static final String[] ENCRYPTION_ALGORITHMS = {"AES/GCM/NoPadding", "AES/CTR/NoPadding",
            "AES/CBC/PKCS5Padding", "AES/ECB/PKCS5Padding", "Serpent/CTR/NoPadding", "Serpent/CBC/ISO10126",
            "Serpent/ECB/ISO10126", "Twofish/CTR/NoPadding", "Twofish/CBC/ISO10126", "Twofish/ECB/ISO10126",
            "Blowfish/CTR/NoPadding", "Blowfish/CBC/PKCS5Padding", "Blowfish/ECB/PKCS5Padding"};
    // PBKDF2 HMAC algorithm
    public static final String HMAC_ALGORITHM = "HMAC-SHA-256";
    // PBKDF2 # of iterations
    public static final int ITERATIONS = 310000;
    // DATE FORMAT
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd@HH:mm");
    // VERSION NUMBER
    public static final String VERSION_NUMBER = "1.3.6";
    // VERIONS COMPATIBLE WITH
    public static final String[] COMPATIBLE_VERSIONS = {"1.3.6", "1.3.5", "1.3.4", "1.3.3", "1.3.2"};
    // STANDARD ECHO CHAR
    public static final char ECHO_CHAR = 0x2022;
    // JFRAME ICON
    public static Image IMAGE;
    public static MainUI ui;

    public static void main(String[] args) {

        loadIconImage();

        ui = new MainUI();

        if (args.length > 0) {

            ui.databaseFile = new File(args[0]);
            new OpenDatabaseUI(new File(args[0]).getAbsolutePath());
        }
    }

    private static void loadIconImage() {

        try {
            IMAGE = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/icon.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
