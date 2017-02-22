package com.elekscamp.messenger_javafx_client.entities;

import org.codehaus.jackson.annotate.JsonProperty;

public class Token {
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("expires_in")
	private int expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setExpiresIn(int excpiresIn) {
		this.expiresIn = excpiresIn;
	}
}
