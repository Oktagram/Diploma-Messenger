package com.elekscamp.messenger_javafx_client.exceptions;

public class ExceptionResolver {
	public void checkForErrorCodeException(int code, String message) throws HttpErrorCodeException {

		if (code == 401)
			throw new HttpErrorCodeException("Only for authorized users.");

		if (code != -1)
			throw new HttpErrorCodeException(message);
	}
}
