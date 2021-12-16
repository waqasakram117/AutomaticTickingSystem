package com.delivery.system.exceptions;

import org.springframework.http.HttpStatus;

class TicketingSystemException extends RuntimeException {

	private static final long serialVersionUID = 6090137084852706397L;
	private final HttpStatus httpStatus;


    public TicketingSystemException(String message) {
        this(message, null);
    }

    public TicketingSystemException(String message, Throwable throwable) {
        this(message, throwable, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TicketingSystemException(final String message, final Throwable throwable, final HttpStatus httpStatus) {
        super(message, throwable);
        this.httpStatus = httpStatus;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
