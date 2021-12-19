package com.delivery.system.ticketing.pojos.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.delivery.system.ticketing.validation.ValidAheadTime;
import com.delivery.system.ticketing.validation.ValidCustomerType;
import com.delivery.system.ticketing.validation.ValidDeliveryStatus;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

class NewDeliveryDtoTest {
	private static Validator validator;
	private NewDeliveryDto deliveryDto;

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
		deliveryDto = prepareValidDeliveryDTO();
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
	void validationFailedForAbsentCustomerType() {
		deliveryDto.setCustomerType(null);
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidCustomerType.ERROR_MESSAGE))
				.count());
	}

	@Test
	void validationFailedForInvalidCustomerType() {
		deliveryDto.setCustomerType("Normal");
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidCustomerType.ERROR_MESSAGE))
				.count());
	}

	@Test
	void validationFailedForAbsentDeliveryStatus() {
		deliveryDto.setDeliveryStatus(null);
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidDeliveryStatus.ERROR_MESSAGE))
				.count());
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
	void validationFailedForAbsentExpectedDeliveryTime() {
		deliveryDto.setExpectedDeliveryTime(null);
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidAheadTime.ERROR_MESSAGE))
				.count());
	}

	@Test
	void validationFailedForInvalidExpectedDeliveryTime() {
		deliveryDto.setExpectedDeliveryTime(UtcDateTimeUtils.utcTimeNow().minusMinutes(5));
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidAheadTime.ERROR_MESSAGE))
				.count());
	}

	@Test
	void validationFailedForNonPositiveDestinationDistance() {
		deliveryDto.setDestinationDistance(0);
		var violations = validator.validate(deliveryDto);

		var errorMsg = "Destination Distance must be logical in meters";
		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(errorMsg))
				.count());
	}

	@Test
	void validationFailedForAbsentRideRating() {
		deliveryDto.setRiderRating(null);
		var violations = validator.validate(deliveryDto);

		var errorMsg = "Rider rating must be between 0-5";
		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(errorMsg))
				.count());
	}

	@Test
	void validationFailedForNegativeRideRating() {
		deliveryDto.setRiderRating(-1);
		var violations = validator.validate(deliveryDto);

		var errorMsg = "Rider rating must be between 0-5";
		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(errorMsg))
				.count());
	}

	@Test
	void validationFailedForNonPositiveFoodPreparationTime() {
		deliveryDto.setFoodPreparationTime(0);
		var violations = validator.validate(deliveryDto);

		var errorMsg = "To prepare food it must take logical and reasonable time in minutes";
		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(errorMsg))
				.count());
	}

	@Test
	void validationFailedForAbsentTimeToReachDestination() {
		deliveryDto.setTimeToReachDestination(null);
		var violations = validator.validate(deliveryDto);

		assertEquals(1L, violations
				.stream()
				.filter(c -> c.getMessage().equals(ValidAheadTime.ERROR_MESSAGE))
				.count());
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


	private NewDeliveryDto prepareValidDeliveryDTO() {

		var now = UtcDateTimeUtils.utcTimeNow();
		var dto = new NewDeliveryDto();
		dto.setDeliveryStatus("Order received");
		dto.setCustomerType("New");
		dto.setRiderRating(5);
		dto.setDestinationDistance(5);
		dto.setTimeToReachDestination(now.plusMinutes(30));
		dto.setExpectedDeliveryTime(now.plusMinutes(50));
		dto.setFoodPreparationTime(10);

		return dto;
	}

}