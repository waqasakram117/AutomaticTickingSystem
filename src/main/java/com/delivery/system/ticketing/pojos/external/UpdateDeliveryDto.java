package com.delivery.system.ticketing.pojos.external;

import com.delivery.system.ticketing.enums.DeliveryStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UpdateDeliveryDto {

	@NotNull(message = "Delivery Id can't be null")
	private Long deliveryId;

	private LocalDateTime timeToReachDestination;

	@Min(value = 1, message = "To prepare food it must take logical and reasonable time in seconds")
	@Max(value = Integer.MAX_VALUE, message = "To prepare food it must take logical and reasonable time in seconds")
	private Integer foodPreparationTime;

	private DeliveryStatus deliveryStatus;

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

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}
