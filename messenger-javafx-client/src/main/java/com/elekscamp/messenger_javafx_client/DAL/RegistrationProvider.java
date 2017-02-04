package com.elekscamp.messenger_javafx_client.DAL;

import java.io.IOException;

import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

public class RegistrationProvider {
	
	private RequestService<User> service;
	
	public RegistrationProvider(RequestService<User> service) {
		this.service = service;
	}
	
	public String register(User user) throws HttpErrorCodeException, IOException {
		return service.post("/registration", user);
	}
}
