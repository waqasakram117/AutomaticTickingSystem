package com.delivery.system.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class TicketingSystemExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(TicketingSystemException.class)
	public ResponseEntity<Object> handleTicketingException(TicketingSystemException ex) {
		return createResponseEntity(ex.getMessage(), ex.getHttpStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return createResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return createResponseEntity("JSON parse error: Some of requested parameters are invalid data type", HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {
		Error errorDto = new Error();
		for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
			errorDto.addErrorField(fieldError.getField(), fieldError.getDefaultMessage());
		}
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(errorDto, httpHeaders, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Object> createResponseEntity(String message, HttpStatus status) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(Map.of("error", message), headers, status);
	}

	private static class Error {

		@JsonProperty(value = "errors")
		private List<ErrorData> errors = new ArrayList<>();

		public void addErrorField(String field, String message) {
			var errorData = new ErrorData(field, message);
			errors.add(errorData);
		}

		public List<ErrorData> getErrors() {
			return errors;
		}

		private static class ErrorData {

			private final String field;

			private final String message;

			public ErrorData(String field, String message) {
				this.field = field;
				this.message = message;
			}

			public String getField() {
				return field;
			}

			public String getMessage() {
				return message;
			}
		}

	}
}
