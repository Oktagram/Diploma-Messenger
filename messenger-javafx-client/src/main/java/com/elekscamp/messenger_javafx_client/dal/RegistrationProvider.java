package com.elekscamp.messenger_javafx_client.dal;

import java.io.IOException;

import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

public class RegistrationProvider {

	private RequestService<User> service;

	public RegistrationProvider(RequestService<User> service) {
		this.service = service;
	}

	public String register(User user) throws HttpErrorCodeException, IOException {
		return service.post("/registration", user);
	}
}
