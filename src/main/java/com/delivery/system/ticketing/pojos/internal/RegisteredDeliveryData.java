package com.delivery.system.ticketing.pojos.internal;

import com.delivery.system.ticketing.enums.CustomerType;
import com.delivery.system.ticketing.enums.DeliveryStatus;

import java.time.LocalDateTime;

public final class RegisteredDeliveryData {

	public final Long id;

	public final String customerType;

	public final String deliveryStatus;

	public final LocalDateTime expectedDeliveryTime;

	public final Integer destinationDistance;

	public final LocalDateTime timeToReachDestination;

	public final LocalDateTime createdAt;

	public final LocalDateTime lastModified;

	public RegisteredDeliveryData(Builder builder) {
		this.customerType = builder.customerType;
		this.createdAt = builder.createdAt;
		this.expectedDeliveryTime = builder.expectedDeliveryTime;
		this.timeToReachDestination = builder.timeToReachDestination;
		this.deliveryStatus = builder.deliveryStatus;
		this.destinationDistance = builder.destinationDistance;
		this.id = builder.id;
		this.lastModified = builder.lastModified;
	}

	public static Builder builder() {
		return new Builder();
	}


	public static final class Builder {
		private Long id;
		private String customerType;
		private String deliveryStatus;
		private LocalDateTime expectedDeliveryTime;
		private Integer destinationDistance;
		private LocalDateTime timeToReachDestination;
		private LocalDateTime createdAt;
		private LocalDateTime lastModified;

		private Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder customerType(CustomerType customerType) {
			this.customerType = customerType.type;
			return this;
		}

		public Builder deliveryStatus(DeliveryStatus deliveryStatus) {
			this.deliveryStatus = deliveryStatus.status;
			return this;
		}

		public Builder expectedDeliveryTime(LocalDateTime expectedDeliveryTime) {
			this.expectedDeliveryTime = expectedDeliveryTime;
			return this;
		}

		public Builder destinationDistance(Integer destinationDistance) {
			this.destinationDistance = destinationDistance;
			return this;
		}

		public Builder timeToReachDestination(LocalDateTime timeToReachDestination) {
			this.timeToReachDestination = timeToReachDestination;
			return this;
		}

		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Builder lastModified(LocalDateTime lastModified) {
			this.lastModified = lastModified;
			return this;
		}

		public RegisteredDeliveryData build() {
			return new RegisteredDeliveryData(this);
		}
	}
}
