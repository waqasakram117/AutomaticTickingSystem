package com.delivery.system.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

class TicketingSystemException extends RuntimeException {

	private static final Logger log = LoggerFactory.getLogger(TicketingSystemException.class);
	private static final long serialVersionUID = 6090137084852706397L;
	private final HttpStatus httpStatus;


	public TicketingSystemException(String message) {
		this(message, null);
	}

	public TicketingSystemException(String message, Throwable throwable) {
		this(message, throwable, HttpStatus.INTERNAL_SERVER_ERROR);
		log.error(message, throwable);
	}

	public TicketingSystemException(final String message, final Throwable throwable, final HttpStatus httpStatus) {
		super(message, throwable);
		this.httpStatus = httpStatus;
	}


	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
