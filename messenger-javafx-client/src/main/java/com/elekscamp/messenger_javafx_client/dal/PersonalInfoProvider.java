package com.elekscamp.messenger_javafx_client.dal;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

public class PersonalInfoProvider extends EntityProvider<PersonalInfo> {

	public PersonalInfoProvider(RequestService<PersonalInfo> service) {
		this.service = service;
		path = "/personalinfo";
	}

	public List<PersonalInfo> getAll() throws HttpErrorCodeException, IOException {
		return service.get(path);
	}

	public List<PersonalInfo> getAll(int page, int pageSize) throws HttpErrorCodeException, IOException {
		return service.get(path, page, pageSize);
	}

	public PersonalInfo getById(int id) throws HttpErrorCodeException, IOException {
		return service.get(path, id);
	}

	public void update(int id, PersonalInfo item) throws HttpErrorCodeException, IOException {
		service.put(path, id, item);
	}

	public void delete(int id) throws HttpErrorCodeException, IOException {
		service.delete(path, id);
	}
}
