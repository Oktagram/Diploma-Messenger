package com.elekscamp.messenger_javafx_client.exceptions;

@SuppressWarnings("serial")
public class HttpErrorCodeException extends Exception {
	public HttpErrorCodeException() {
	}

	public HttpErrorCodeException(String message) {
		super(message);
	}

	public HttpErrorCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpErrorCodeException(Throwable cause) {
		super(cause);
	}
}
