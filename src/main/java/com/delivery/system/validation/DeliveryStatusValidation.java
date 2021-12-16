package com.delivery.system.validation;

import com.delivery.system.ticketing.enums.DeliveryStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DeliveryStatusValidation implements ConstraintValidator<ValidDeliveryStatus, String> {

	private String violationMsg;

	@Override
	public void initialize(ValidDeliveryStatus constraintAnnotation) {
		violationMsg = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(String status, ConstraintValidatorContext context) {

		if (DeliveryStatus.getByStatus(status) == DeliveryStatus.INVALID) {
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
