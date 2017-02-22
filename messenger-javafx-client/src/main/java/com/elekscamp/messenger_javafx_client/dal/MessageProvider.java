package com.elekscamp.messenger_javafx_client.dal;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.entities.Message;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

public class MessageProvider extends EntityProvider<Message> {

	public MessageProvider(RequestService<Message> service) {
		this.service = service;
		path = "/messages";
	}

	public List<Message> getAll() throws HttpErrorCodeException, IOException {
		return service.get(path);
	}

	public List<Message> getAll(int page, int pageSize) throws HttpErrorCodeException, IOException {
		return service.get(path, page, pageSize);
	}

	public Message getById(int id) throws HttpErrorCodeException, IOException {
		return service.get(path, id);
	}

	public List<Message> getByConversationId(Integer id) throws HttpErrorCodeException, IOException {
		return service.get(path + "/byConversation/" + id.toString());
	}

	public List<Message> getByConversationId(Integer id, int page, int pageSize)
			throws HttpErrorCodeException, IOException {
		return service.get(path + "/byConversation/" + id.toString(), page, pageSize);
	}

	public Message add(Message item) throws HttpErrorCodeException, IOException {

		response = service.post(path, item);
		createdItem = objectMapper.readValue(response, Message.class);
		return createdItem;
	}

	public void update(int id, Message item) throws HttpErrorCodeException, IOException {
		service.put(path, id, item);
	}

	public void delete(int id) throws HttpErrorCodeException, IOException {
		service.delete(path, id);
	}
}
