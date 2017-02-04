package com.elekscamp.messenger_javafx_client.DAL;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

public class UserProvider extends EntityProvider<User> {

	public UserProvider(RequestService<User> service) {
		this.service = service;
		path = "/users";
	}

	public List<User> getAll() throws HttpErrorCodeException, IOException {
		return service.get(path);
	}

	public List<User> getAll(int page, int pageSize) throws HttpErrorCodeException, IOException {
		return service.get(path, page, pageSize);
	}
	
	public User getById(int id) throws HttpErrorCodeException, IOException {
		return service.get(path, id);
	}
	
	public List<User> getFriendsById(Integer id) throws HttpErrorCodeException, IOException {
		return service.get(path + "/" + id.toString() + "/friends");
	}
	
	public List<User> getByConversationId(Integer id) throws HttpErrorCodeException, IOException {
		return service.get(path + "/byConversation/" + id.toString());
	}
	
	public List<User> getByConversationId(Integer id, int page, int pageSize) throws HttpErrorCodeException, IOException {
		return service.get(path + "/byConversation/" + id.toString(), page, pageSize);
	}

	public User add(User item) throws HttpErrorCodeException, IOException {
		
		response = service.post(path, item);
		createdItem = objectMapper.readValue(response, User.class);
		return createdItem;
	}

	public void update(int id, User item) throws HttpErrorCodeException, IOException {
		service.put(path, id, item);
	}
	
	public void delete(int id) throws HttpErrorCodeException, IOException {
		service.delete(path, id);
	}
}
