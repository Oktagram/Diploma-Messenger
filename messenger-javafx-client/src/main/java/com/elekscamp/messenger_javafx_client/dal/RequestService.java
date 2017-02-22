package com.elekscamp.messenger_javafx_client.dal;

import java.io.IOException;
import java.util.List;

import com.elekscamp.messenger_javafx_client.exceptions.HttpErrorCodeException;

public interface RequestService<T> {
	public List<T> get(String path) throws HttpErrorCodeException, IOException;

	public List<T> get(String path, Integer page, Integer pageSize) throws HttpErrorCodeException, IOException;

	public T get(String path, Integer id) throws HttpErrorCodeException, IOException;

	public String post(String path, T item) throws HttpErrorCodeException, IOException;

	public T put(String path, Integer id, T item) throws HttpErrorCodeException, IOException;

	public void delete(String path, Integer id) throws HttpErrorCodeException, IOException;
}
