package com.delivery.system.ticketing.pojos.external;

import com.delivery.system.validation.ValidAheadTime;
import com.delivery.system.validation.ValidCustomerType;
import com.delivery.system.validation.ValidDeliveryStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class NewDeliveryDto {

	@ValidCustomerType
	private String customerType;

	@ValidDeliveryStatus
	private String deliveryStatus;

	@ValidAheadTime
	private LocalDateTime expectedDeliveryTime;

	@Positive
	private int destinationDistance;

	@Min(value = 0, message = "Rider Minimum Rating can be 0")
	@Max(value = 5, message = "Rider Maximum Rating can be 5")
	private int riderRating;

	@Positive(message = "To prepare food it must take logical and reasonable time in seconds")
	private int foodPreparationTime;

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

	public int getDestinationDistance() {
		return destinationDistance;
	}

	public void setDestinationDistance(int destinationDistance) {
		this.destinationDistance = destinationDistance;
	}

	public int getRiderRating() {
		return riderRating;
	}

	public void setRiderRating(int riderRating) {
		this.riderRating = riderRating;
	}

	public int getFoodPreparationTime() {
		return foodPreparationTime;
	}

	public void setFoodPreparationTime(int foodPreparationTime) {
		this.foodPreparationTime = foodPreparationTime;
	}

	public LocalDateTime getTimeToReachDestination() {
		return timeToReachDestination;
	}

	public void setTimeToReachDestination(LocalDateTime timeToReachDestination) {
		this.timeToReachDestination = timeToReachDestination;
	}

}
