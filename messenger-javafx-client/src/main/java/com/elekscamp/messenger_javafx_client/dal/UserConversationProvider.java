package com.elekscamp.messenger_javafx_client.dal;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.entities.UserConversation;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

public class UserConversationProvider extends EntityProvider<UserConversation> {

	public UserConversationProvider(RequestService<UserConversation> service) {
		this.service = service;
		path = "/userConversation/";
	}

	public List<UserConversation> getAll() throws HttpErrorCodeException, IOException {
		return service.get(path);
	}

	public UserConversation addUser(int conversationId, int userId) throws HttpErrorCodeException, IOException {

		response = service.post(path, new UserConversation(userId, conversationId));
		createdItem = objectMapper.readValue(response, UserConversation.class);
		return createdItem;
	}

	public void removeUser(Integer conversationId, Integer userId) throws HttpErrorCodeException, IOException {
		service.delete(path + userId.toString(), conversationId);
	}
}
