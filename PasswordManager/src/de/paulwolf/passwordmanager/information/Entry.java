package de.paulwolf.passwordmanager.information;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import de.paulwolf.passwordmanager.Main;

public class Entry {
	
	private String title;
	private String username;
	private String email;
	private String password;
	private Date lastModified;
	
	// CONSTRUCTOR FOR CREATING AN ENTRY
	public Entry(String title, String username, String email, String password) {
		this.title = title;
		this.username = username;
		this.email = email;
		this.password = password;
		this.lastModified = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	// CONSTRUCTOR ONLY FOR LOADING DATA
	public Entry(String title, String username, String email, String password, Date lastModified) {
		this.title = title;
		this.username = username;
		this.email = email;
		this.password = password;
		this.lastModified = lastModified;
	}
	
	public String[] getInformationAsArray() {
		
		String[] arr = new String[5];
		
		arr[0] = this.title;
		arr[1] = this.username;
		arr[2] = this.email;
		arr[3] = this.password;
		arr[4] = Main.DATE_FORMAT.format(this.lastModified);
		
		return arr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
