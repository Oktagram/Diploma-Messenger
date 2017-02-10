package com.elekscamp.messenger_javafx_client.DAL;

import org.codehaus.jackson.map.ObjectMapper;

public abstract class EntityProvider<T> {

	protected String path;
	protected ObjectMapper objectMapper;
	protected String response;
	protected T createdItem;
	protected RequestService<T> service;

	public EntityProvider() {
		objectMapper = new ObjectMapper();
	}
}
