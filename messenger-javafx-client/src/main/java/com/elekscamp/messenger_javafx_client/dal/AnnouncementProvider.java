package com.elekscamp.messenger_javafx_client.dal;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.entities.Announcement;
import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

public class AnnouncementProvider extends EntityProvider<Announcement>{

	public AnnouncementProvider(RequestService<Announcement> service) {
		this.service = service;
		path = "/announcements";
	}
	
	public List<Announcement> getAll() throws HttpErrorCodeException, IOException {
		return service.get(path);
	}
	
	public Announcement update(int id, Announcement item) throws HttpErrorCodeException, IOException {
		return service.put(path, id, item);
	}
	
	public Announcement add(Announcement item) throws HttpErrorCodeException, IOException {
		
		response = service.post(path, item);
		createdItem = objectMapper.readValue(response, Announcement.class);
		return createdItem;
	}
}
