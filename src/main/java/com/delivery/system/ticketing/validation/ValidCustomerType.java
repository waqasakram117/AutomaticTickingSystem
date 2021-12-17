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
@Constraint(validatedBy = {CustomerTypeValidation.class})
public @interface ValidCustomerType {

	String message() default "Invalid Customer Type. Customer Type must be (VIP, Loyal, New)";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

