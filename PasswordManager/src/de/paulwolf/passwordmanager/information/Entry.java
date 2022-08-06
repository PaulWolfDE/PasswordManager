package de.paulwolf.passwordmanager.information;

import de.paulwolf.passwordmanager.Main;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Entry {

    private final String title;
    private final String username;
    private final String email;
    private final Date lastModified;
    private final String notes;
    private final SecretKey password;

    // CONSTRUCTOR FOR CREATING AN ENTRY
    public Entry(String title, String username, String email, byte[] password, String notes) {
        this.title = title;
        this.username = username;
        this.email = email;
        this.password = new EntryKeySpec(password);
        this.lastModified = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        this.notes = notes;
    }

    // CONSTRUCTOR ONLY FOR LOADING DATA
    public Entry(String title, String username, String email, byte[] password, Date lastModified, String notes) {
        this.title = title;
        this.username = username;
        this.email = email;
        this.password = new EntryKeySpec(password);
        this.lastModified = lastModified;
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getNotes() {
        return notes;
    }

    public byte[] getPassword() {
        return this.password.getEncoded();
    }

    public String[] getAsteriskArray() {

        String[] asteriskArray = new String[6];

        asteriskArray[0] = this.getTitle();
        asteriskArray[1] = this.getUsername();
        asteriskArray[2] = this.getEmail();
        asteriskArray[3] = new String(new char[this.getPassword().length]).replace('\0', Main.ECHO_CHAR);
        asteriskArray[4] = Main.DATE_FORMAT.format(this.getLastModified());
        asteriskArray[5] = this.getNotes();

        return asteriskArray;
    }
}
