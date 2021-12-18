package com.delivery.system.ticketing.validation;

import com.delivery.system.ticketing.enums.DeliveryStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DeliveryStatusValidation implements ConstraintValidator<ValidDeliveryStatus, String> {

	private String violationMsg;
	private boolean isNullAllowed;

	@Override
	public void initialize(ValidDeliveryStatus constraintAnnotation) {
		violationMsg = constraintAnnotation.message();
		isNullAllowed = constraintAnnotation.isNullAllowed();
	}

	@Override
	public boolean isValid(String status, ConstraintValidatorContext context) {

		if (isNullAllowed && status == null) return true;

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
