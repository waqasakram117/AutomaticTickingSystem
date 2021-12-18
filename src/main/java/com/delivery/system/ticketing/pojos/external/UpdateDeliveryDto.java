package com.delivery.system.ticketing.pojos.external;

import com.delivery.system.ticketing.validation.ValidAheadTime;
import com.delivery.system.ticketing.validation.ValidDeliveryStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class UpdateDeliveryDto {

	private static final String FOOD_PREPARATION_VALIDATION_ERROR = "To prepare food it must take logical and reasonable time in minutes";

	@NotNull(message = "Delivery Id can't be null")
	@Positive(message = "Invalid Delivery Id")
	private Long deliveryId;

	@ValidAheadTime(isNullAllowed = true)
	private LocalDateTime timeToReachDestination;

	@Min(value = 0, message = FOOD_PREPARATION_VALIDATION_ERROR)
	@Max(value = Integer.MAX_VALUE, message = FOOD_PREPARATION_VALIDATION_ERROR)
	private Integer foodPreparationTime;

	@ValidDeliveryStatus(isNullAllowed = true)
	private String deliveryStatus;

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public LocalDateTime getTimeToReachDestination() {
		return timeToReachDestination;
	}

	public void setTimeToReachDestination(LocalDateTime timeToReachDestination) {
		this.timeToReachDestination = timeToReachDestination;
	}

	public Integer getFoodPreparationTime() {
		return foodPreparationTime;
	}

	public void setFoodPreparationTime(Integer foodPreparationTime) {
		this.foodPreparationTime = foodPreparationTime;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}
