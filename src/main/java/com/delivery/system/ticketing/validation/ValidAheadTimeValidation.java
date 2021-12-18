package com.delivery.system.ticketing.validation;

import com.delivery.system.utils.UtcDateTimeUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ValidAheadTimeValidation implements ConstraintValidator<ValidAheadTime, LocalDateTime> {

	private String violationMsg;
	private boolean isNullAllowed;

	@Override
	public void initialize(ValidAheadTime constraintAnnotation) {
		violationMsg = constraintAnnotation.message();
		isNullAllowed = constraintAnnotation.isNullAllowed();
	}

	@Override
	public boolean isValid(LocalDateTime time, ConstraintValidatorContext context) {

		if (isNullAllowed && time == null) return true;

		if (time == null || UtcDateTimeUtils.utcTimeNow().isAfter(time)) {
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
