package com.delivery.system.ticketing.enums;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.Map;
import java.util.stream.Stream;

public enum TicketStatus {

	OPEN(1),
	CLOSE(2),
	INVALID(0);

	private static final Map<Short, TicketStatus> ticketStatusByValue =
			Stream.of(values()).filter(c -> c != INVALID).collect(toUnmodifiableMap(c -> c.status, identity()));

	public final short status;

	TicketStatus(Integer status) {
		this.status = status.shortValue();
	}

	public static TicketStatus getByValue(short value) {
		return ticketStatusByValue.getOrDefault(value, INVALID);
	}
}
