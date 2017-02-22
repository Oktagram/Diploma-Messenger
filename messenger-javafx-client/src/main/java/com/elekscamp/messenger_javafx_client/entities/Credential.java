package com.elekscamp.messenger_javafx_client.entities;

public class Credential {
	private String username;
	private String password;

	public Credential(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String toString() {
		return "username=" + username + "&password=" + password;
	}
}
