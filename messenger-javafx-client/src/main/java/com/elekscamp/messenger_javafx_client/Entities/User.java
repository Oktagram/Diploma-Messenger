package com.elekscamp.messenger_javafx_client.Entities;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	
	private int id;
	private String login;
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	private String password;
	private String email;
	private long registrationDate;
	private boolean isOnline;

	public User() { }
	
	public User(String login, String password, String email) {
		
		this.login = login;
		this.password = password;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", email=" + email
				+ ", registrationDate=" + new Timestamp(registrationDate) + ", isOnline=" + isOnline + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(long registrationDate) {
		this.registrationDate = registrationDate;
	}

	public boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
}