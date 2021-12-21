package com.delivery.system.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class BadRequestException extends TicketingSystemException {

	private static final long serialVersionUID = -7330956956660870401L;
	private static final Logger log = LoggerFactory.getLogger(BadRequestException.class);


	public BadRequestException(String message) {
		this(message, null);
	}

	public BadRequestException(String message, Throwable throwable) {
		super(message, throwable, HttpStatus.BAD_REQUEST);
		log.warn(message, throwable);
	}
}
