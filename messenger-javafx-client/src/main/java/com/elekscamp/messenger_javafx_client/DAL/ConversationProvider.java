package com.elekscamp.messenger_javafx_client.DAL;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.Entities.Conversation;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;

public class ConversationProvider extends EntityProvider<Conversation> {

	private final String byUser;

	public ConversationProvider(RequestService<Conversation> service) {
		this.service = service;
		path = "/conversations";
		byUser = "/byUser/";
	}

	public List<Conversation> getAll() throws HttpErrorCodeException, IOException {
		return service.get(path);
	}

	public List<Conversation> getAll(int page, int pageSize) throws HttpErrorCodeException, IOException {
		return service.get(path, page, pageSize);
	}

	public Conversation getById(int id) throws HttpErrorCodeException, IOException {
		return service.get(path, id);
	}

	public List<Conversation> getByUserId(Integer id) throws HttpErrorCodeException, IOException {
		return service.get(path + byUser + id.toString());
	}

	public List<Conversation> getByUserId(Integer id, int page, int pageSize)
			throws HttpErrorCodeException, IOException {
		return service.get(path + byUser + id.toString(), page, pageSize);
	}

	public Conversation add(Conversation item) throws HttpErrorCodeException, IOException {

		response = service.post(path, item);
		createdItem = objectMapper.readValue(response, Conversation.class);
		return createdItem;
	}

	public void update(int id, Conversation item) throws HttpErrorCodeException, IOException {
		service.put(path, id, item);
	}

	public void delete(int id) throws HttpErrorCodeException, IOException {
		service.delete(path, id);
	}
}
