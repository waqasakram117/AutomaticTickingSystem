package com.delivery.system.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class NotFoundException extends TicketingSystemException {

	private static final long serialVersionUID = -1836798593404418926L;
	private static final Logger log = LoggerFactory.getLogger(NotFoundException.class);


	public NotFoundException(String message) {
		this(message, null);
	}

	public NotFoundException(String message, Throwable throwable) {
		super(message, throwable, HttpStatus.NOT_FOUND);
		log.warn(message, throwable);
	}
}
