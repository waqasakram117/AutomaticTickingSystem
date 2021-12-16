package com.delivery.system.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@ControllerAdvice
public class TicketingSystemExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TicketingSystemException.class)
    public ResponseEntity<Object> handleContractException(TicketingSystemException ex) {
        return createResponseEntity(ex.getMessage(), ex.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders httpHeaders, HttpStatus httpStatus, WebRequest webRequest) {
        ValidationError validationErrorDto = new ValidationError();
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            validationErrorDto.addValidationErrorField(fieldError.getField(), fieldError.getDefaultMessage());
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(validationErrorDto, httpHeaders, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> createResponseEntity(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("message", message);
        return new ResponseEntity<>(
                Map.of("errors", List.of(Map.of("message", message))),
                headers, status
        );
    }

    private static class ValidationError{

        private static class ValidationErrorData {

            private final String field;

            private final String message;

            public ValidationErrorData(String field, String message) {
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
        @JsonProperty(value = "errors")
        private List<ValidationErrorData> validationErrors = new ArrayList<>();


        public void addValidationErrorField(String field, String message) {
            var errorData = new ValidationErrorData(field, message);
            validationErrors.add(errorData);
        }


        public List<ValidationErrorData> getValidationErrors() {
            return validationErrors;
        }

    }
}
