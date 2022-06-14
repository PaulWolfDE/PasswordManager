package de.paulwolf.passwordmanager.information;

import de.paulwolf.passwordmanager.Main;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Entry {

    private final String title;
    private final String username;
    private final String email;
    private final Date lastModified;
    private final String notes;
    private final String password;

    // CONSTRUCTOR FOR CREATING AN ENTRY
    public Entry(String title, String username, String email, String password, String notes) {
        this.title = title;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastModified = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        this.notes = notes;
    }

    // CONSTRUCTOR ONLY FOR LOADING DATA
    public Entry(String title, String username, String email, String password, Date lastModified, String notes) {
        this.title = title;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastModified = lastModified;
        this.notes = notes;
    }

    public String[] getInformationAsArray() {

        String[] arr = new String[6];

        arr[0] = this.title;
        arr[1] = this.username;
        arr[2] = this.email;
        arr[3] = this.password;
        arr[4] = Main.DATE_FORMAT.format(this.lastModified);
        arr[5] = this.notes;

        return arr;
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

    // DEBUG
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] arr = this.getInformationAsArray();
        for (int i = 0; i < 6; i++) {
            sb.append(arr[i]);
            if (i < 5)
                sb.append(", ");
        }
        return sb.toString();
    }

    public String getPassword() {
        return this.password;
    }
}
