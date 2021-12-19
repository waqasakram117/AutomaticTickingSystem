package com.delivery.system.ticketing.pojos.external;

import com.delivery.system.ticketing.validation.ValidAheadTime;
import com.delivery.system.ticketing.validation.ValidCustomerType;
import com.delivery.system.ticketing.validation.ValidDeliveryStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class NewDeliveryDto {

	private static final String RIDING_VALIDATION_ERROR_MSG = "Rider rating must be between 0-5";

	@ValidCustomerType
	private String customerType;

	@ValidDeliveryStatus
	private String deliveryStatus;

	@ValidAheadTime
	private LocalDateTime expectedDeliveryTime;

	@Positive(message = "Destination Distance must be logical in meters")
	private Integer destinationDistance;


	@NotNull(message = RIDING_VALIDATION_ERROR_MSG)
	@Min(value = 0, message = RIDING_VALIDATION_ERROR_MSG)
	@Max(value = 5, message = RIDING_VALIDATION_ERROR_MSG)
	private Integer riderRating;

	@Positive(message = "To prepare food it must take logical and reasonable time in minutes")
	private Integer foodPreparationTime;

	@ValidAheadTime
	private LocalDateTime timeToReachDestination;


	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public LocalDateTime getExpectedDeliveryTime() {
		return expectedDeliveryTime;
	}

	public void setExpectedDeliveryTime(LocalDateTime expectedDeliveryTime) {
		this.expectedDeliveryTime = expectedDeliveryTime;
	}

	public Integer getDestinationDistance() {
		return destinationDistance;
	}

	public void setDestinationDistance(Integer destinationDistance) {
		this.destinationDistance = destinationDistance;
	}

	public Integer getRiderRating() {
		return riderRating;
	}

	public void setRiderRating(Integer riderRating) {
		this.riderRating = riderRating;
	}

	public Integer getFoodPreparationTime() {
		return foodPreparationTime;
	}

	public void setFoodPreparationTime(Integer foodPreparationTime) {
		this.foodPreparationTime = foodPreparationTime;
	}

	public LocalDateTime getTimeToReachDestination() {
		return timeToReachDestination;
	}

	public void setTimeToReachDestination(LocalDateTime timeToReachDestination) {
		this.timeToReachDestination = timeToReachDestination;
	}

}
