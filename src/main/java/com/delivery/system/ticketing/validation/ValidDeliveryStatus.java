package com.delivery.system.ticketing.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {DeliveryStatusValidation.class})
public @interface ValidDeliveryStatus {

	String ERROR_MESSAGE = "Invalid Delivery Status. Status must be (Order received, Order Preparing, Order Pickedup, Order Delivered)";

	String message() default ERROR_MESSAGE;

	boolean isNullAllowed() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

