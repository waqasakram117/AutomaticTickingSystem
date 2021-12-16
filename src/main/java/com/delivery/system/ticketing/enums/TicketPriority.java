package com.delivery.system.ticketing.enums;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.Map;
import java.util.stream.Stream;

public enum TicketPriority {

	HIGH(1),
	MEDIUM(2),
	LOW(0),
	INVALID(-1);

	private static final Map<Short, TicketPriority> ticketPriorityByValue =
			Stream.of(values()).filter(c -> c != INVALID).collect(toUnmodifiableMap(c -> c.value, identity()));

	public final short value;

	TicketPriority(Integer value) {
		this.value = value.shortValue();
	}

	public static TicketPriority getByValue(short value) {
		return ticketPriorityByValue.getOrDefault(value, INVALID);
	}

}