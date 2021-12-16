package com.delivery.system.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends TicketingSystemException{

	private static final long serialVersionUID = -7330956956660870401L;

	public BadRequestException(String message) {
		super(message, null);
	}

	public BadRequestException(String message, Throwable throwable) {
		super(message, throwable, HttpStatus.BAD_REQUEST);
	}
}
