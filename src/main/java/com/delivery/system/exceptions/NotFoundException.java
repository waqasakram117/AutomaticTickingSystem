package com.delivery.system.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends TicketingSystemException {

	private static final long serialVersionUID = -1836798593404418926L;

	public NotFoundException(String message) {
		super(message, null);
	}

	public NotFoundException(String message, Throwable throwable) {
		super(message, throwable, HttpStatus.NOT_FOUND);
	}
}
