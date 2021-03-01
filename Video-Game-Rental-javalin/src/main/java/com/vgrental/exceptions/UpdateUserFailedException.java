package com.vgrental.exceptions;

public class UpdateUserFailedException extends RuntimeException {
	public UpdateUserFailedException() {
	}

	public UpdateUserFailedException(String message) {
		super(message);
	}

	public UpdateUserFailedException(Throwable cause) {
		super(cause);
	}
	
	public UpdateUserFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateUserFailedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
