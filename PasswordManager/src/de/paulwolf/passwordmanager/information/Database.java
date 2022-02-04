package de.paulwolf.passwordmanager.information;

import java.io.File;
import java.util.ArrayList;

import de.paulwolf.passwordmanager.Main;

public class Database {

	private File path;
	private byte[] masterKey;
	private String hashAlgorithm = Main.HASH_ALGORITHM;
	private String encryptionAlgorithm = Main.ENCRYPTION_ALGORITHM;
	
	private ArrayList<Entry> entries = new ArrayList<Entry>();

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public byte[] getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(byte[] masterKey) {
		this.masterKey = masterKey;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}
	
	public ArrayList<Entry> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<Entry> entries) {
		this.entries = entries;
	}

	public void addEntry(Entry entry) {
		this.entries.add(entry);
	}

	public String getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	public void setEncryptionAlgorithm(String encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}
}
