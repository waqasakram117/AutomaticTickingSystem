package com.delivery.system.ticketing.enums;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.Map;
import java.util.stream.Stream;

public enum DeliveryStatus {

	RECEIVED("Order received"),
	PREPARING("Order Preparing"),
	PICKEDUP("Order Pickedup"),
	DELIEVERED("Order Delivered"),
	INVALID("Invalid");

	private static final Map<String, DeliveryStatus> deliveryByStatuses =
			Stream.of(values()).filter(c -> c != INVALID).collect(toUnmodifiableMap(c -> c.status, identity()));
	public final String status;

	DeliveryStatus(String status) {
		this.status = status;
	}

	public static boolean isReceived(String status) {
		return RECEIVED.status.equalsIgnoreCase(status);
	}

	public static boolean isPreparing(String status) {
		return PREPARING.status.equalsIgnoreCase(status);
	}

	public static boolean isPickedup(String status) {
		return PICKEDUP.status.equalsIgnoreCase(status);
	}

	public static boolean isDelivered(String status) {
		return DELIEVERED.status.equalsIgnoreCase(status);
	}

	public static DeliveryStatus getByStatus(String status) {
		if (null == status)
			return INVALID;

		return deliveryByStatuses.getOrDefault(status, INVALID);
	}

	public boolean isInvalid() {
		return this == INVALID;
	}

	@Override
	public String toString() {
		return this.status;
	}
}