package com.delivery.system.ticketing.enums;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.Map;
import java.util.stream.Stream;

public enum CustomerType {

	VIP("VIP"),
	LOYAL("Loyal"),
	NEW("New"),
	INVALID("Invalid");

	private static final Map<String, CustomerType> customerByTypes =
			Stream.of(values()).filter(c -> c != INVALID).collect(toUnmodifiableMap(c -> c.type, identity()));

	public final String type;

	CustomerType(String type) {
		this.type = type;
	}

	public static boolean isVIP(String type) {
		return VIP.type.equalsIgnoreCase(type);
	}

	public static boolean isLoyal(String type) {
		return LOYAL.type.equalsIgnoreCase(type);
	}

	public static boolean isNew(String type) {
		return NEW.type.equalsIgnoreCase(type);
	}

	public static CustomerType getByType(String type) {
		if (null == type)
			return INVALID;

		return customerByTypes.getOrDefault(type, INVALID);
	}

	public boolean isInvalid() {
		return this == INVALID;
	}

	@Override
	public String toString() {
		return this.type;
	}
}