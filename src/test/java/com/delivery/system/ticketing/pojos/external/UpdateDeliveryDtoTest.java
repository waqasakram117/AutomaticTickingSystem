package com.delivery.system.ticketing.pojos.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.delivery.system.ticketing.validation.ValidAheadTime;
import com.delivery.system.ticketing.validation.ValidDeliveryStatus;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

class UpdateDeliveryDtoTest {

	private static Validator validator;
	private UpdateDeliveryDto deliveryDto;

	@BeforeAll
	static void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@AfterAll
	static void tearDown() {
		validator = null;
	}

	@BeforeEach
	public void registerData() {
		deliveryDto = prepareValidUpdateDeliveryDTO();
	}

	@AfterEach
	public void deregisterData() {
		deliveryDto = null;
	}

	@Test
	void validationPassedForValidDeliveryDto() {
		var violations = validator.validate(deliveryDto);
		assertTrue(violations.isEmpty());
	}

	@Test
	void validationPassedForAbsentDeliveryStatus() {
		deliveryDto.setDeliveryStatus(null);
		var violations = validator.validate(deliveryDto);

		assertTrue(violations.isEmpty());
	}

	@Test
	void validationFailedForInvalidDeliveryStatus() {
		deliveryDto.setDeliveryStatus("Order Shifted");
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidDeliveryStatus.ERROR_MESSAGE))
				.count());
	}

	@Test
	void validationPassedForAbsentTimeToReachDestination() {
		deliveryDto.setTimeToReachDestination(null);
		var violations = validator.validate(deliveryDto);

		assertTrue(violations.isEmpty());
	}

	@Test
	void validationFailedForInvalidTimeToReachDestination() {
		deliveryDto.setTimeToReachDestination(UtcDateTimeUtils.utcTimeNow().minusMinutes(5));
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidAheadTime.ERROR_MESSAGE))
				.count());
	}

	@Test
	void validationPassedForSettingFoodPreparationTimeInstantly() {
		deliveryDto.setFoodPreparationTime(0);
		var violations = validator.validate(deliveryDto);

		assertTrue(violations.isEmpty());
	}

	@Test
	void validationFailedForNegativeFoodPreparationTime() {
		deliveryDto.setFoodPreparationTime(-1);
		var violations = validator.validate(deliveryDto);

		var errorMsg = "To prepare food it must take logical and reasonable time in minutes";
		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(errorMsg))
				.count());
	}

	private UpdateDeliveryDto prepareValidUpdateDeliveryDTO() {
		var now = UtcDateTimeUtils.utcTimeNow();
		var dto = new UpdateDeliveryDto();
		dto.setDeliveryId(1L);
		dto.setDeliveryStatus("Order received");
		dto.setTimeToReachDestination(now.plusMinutes(30));
		dto.setFoodPreparationTime(10);

		return dto;
	}

}