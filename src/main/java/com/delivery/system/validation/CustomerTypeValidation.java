package com.delivery.system.validation;

import com.delivery.system.ticketing.enums.CustomerType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerTypeValidation implements ConstraintValidator<ValidCustomerType, String> {

	private String violationMsg;

	@Override
	public void initialize(ValidCustomerType constraintAnnotation) {
		violationMsg = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(String type, ConstraintValidatorContext context) {

		if (CustomerType.getByType(type) == CustomerType.INVALID) {
			updateContextValidator(context, violationMsg);

			return false;
		}

		return true;
	}

	private void updateContextValidator(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message)
				.addConstraintViolation();
	}

}
