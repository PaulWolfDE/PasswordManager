package de.paulwolf.passwordmanager;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.ui.DatabaseUI;
import de.paulwolf.passwordmanager.ui.MainUI;
import de.paulwolf.passwordmanager.ui.OpenDatabaseUI;

public class Main {

	// STANDARD ECHO CHAR
	public static char ECHO_CHAR = 0x2022;

	// JFRAME ICON
	public static Image IMAGE;

	// STANDARD HASH ALGORITHM
	public static final String HASH_ALGORITHM = "SHA-256";
	public static final String[] HASH_ALGORITHMS = { "SHA-256", "MD5" };

	// STANDARD ENCRYPTION ALGORITHM
	public static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
	public static final String[] ENCRYPTION_ALGORITHMS = { "AES/GCM/NoPadding", "AES/CTR/NoPadding",
			"AES/CBC/PKCS5Padding", "AES/ECB/PKCS5Padding", "Serpent/CTR/NoPadding", "Serpent/CBC/ISO10126",
			"Serpent/ECB/ISO10126", "Twofish/CTR/NoPadding", "Twofish/CBC/ISO10126", "Twofish/ECB/ISO10126",
			"Blowfish/CTR/NoPadding", "Blowfish/CBC/PKCS5Padding", "Blowfish/ECB/PKCS5Padding" };

	// DATE FORMAT
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd@HH:mm");

	// VERSION NUMBER
	public static final String VERSION_NUMBER = "1.3.3";

	// VERIONS COMPATIBLE WITH
	public static final String[] COMPATIBLE_VERSIONS = { "1.3.3", "1.3.2" };

	public static final boolean DEBUG = false;

	public static Database db = new Database();

	public static MainUI ui;
	
	public static DatabaseUI dbui;

	public static void main(String[] args) {

		loadIconImage();
		ui = new MainUI();
		
		if (args.length > 0) {
			MainUI.databaseFile = new File(args[0]);
			new OpenDatabaseUI();
		} else
			ui.initUI();

	}

	private static void loadIconImage() {

		try {
			IMAGE = ImageIO.read(Main.class.getResource("/icon.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
