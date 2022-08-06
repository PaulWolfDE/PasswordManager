package de.paulwolf.passwordmanager.information;

import de.paulwolf.passwordmanager.Main;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.ArrayList;

public class Database {

    private File path;
    private SecretKey masterKey;
    private String hashAlgorithm = Main.HASH_ALGORITHM;
    private String encryptionAlgorithm = Main.ENCRYPTION_ALGORITHM;

    private ArrayList<Entry> entries = new ArrayList<>();

    public byte[] getMasterKey() {
        return masterKey.getEncoded();
    }

    public void setMasterKey(SecretKey masterKey) {
        this.masterKey = masterKey;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
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
