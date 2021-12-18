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
@Constraint(validatedBy = {ValidAheadTimeValidation.class})
public @interface ValidAheadTime {

	String message() default "Requested time has been passed. Give future valid time";

	boolean isNullAllowed() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

