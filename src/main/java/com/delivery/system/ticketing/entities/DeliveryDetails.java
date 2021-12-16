package com.delivery.system.ticketing.entities;

import com.delivery.system.ticketing.entities.convertors.CustomerTypeConverter;
import com.delivery.system.ticketing.entities.convertors.DeliveryStatusConverter;
import com.delivery.system.ticketing.enums.CustomerType;
import com.delivery.system.ticketing.enums.DeliveryStatus;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Table(name = "DELIVERY_DETAILS")
@Entity()
public class DeliveryDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id", columnDefinition = "INT UNSIGNED", nullable = false)
	private Long id;

	@Column(name = "customer_type")
	@Convert(converter = CustomerTypeConverter.class)
	private CustomerType customerType;

	@Column(name = "delivery_status")
	@Convert(converter = DeliveryStatusConverter.class)
	private DeliveryStatus deliveryStatus;

	@Column(name = "expected_delivery_time")
	private LocalDateTime expectedDeliveryTime;

	@Positive
	@Column(name = "current_distance_from_destination_in_meters")
	private Integer distanceFromDestination;

	@Column(name = "time_to_reach_destination")
	private LocalDateTime timeToReachDestination;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public LocalDateTime getExpectedDeliveryTime() {
		return expectedDeliveryTime;
	}

	public void setExpectedDeliveryTime(LocalDateTime expectedDeliveryTime) {
		this.expectedDeliveryTime = expectedDeliveryTime;
	}

	public Integer getDistanceFromDestination() {
		return distanceFromDestination;
	}

	public void setDistanceFromDestination(Integer distanceFromDestination) {
		this.distanceFromDestination = distanceFromDestination;
	}

	public LocalDateTime getTimeToReachDestination() {
		return timeToReachDestination;
	}

	public void setTimeToReachDestination(LocalDateTime timeToReachDestination) {
		this.timeToReachDestination = timeToReachDestination;
	}
}
