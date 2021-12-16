package com.delivery.system.utils;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

public final class BeanValidator {
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	private BeanValidator() {
	}

	public static <T> void validate(T bean) {
		var result = validator.validate(bean);
		if (!result.isEmpty()) throw new ConstraintViolationException(result);
	}

	public static <T> void validate(T bean, Class<?>... group) {
		var result = validator.validate(bean, group);
		if (!result.isEmpty()) throw new ConstraintViolationException(result);
	}
}