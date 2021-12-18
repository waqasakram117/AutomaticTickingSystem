package com.delivery.system.ticketing.mappers;

import com.delivery.system.ticketing.enums.CustomerType;
import com.delivery.system.ticketing.enums.TicketPriority;

public final class TicketPriorityMapper {
	private TicketPriorityMapper() {
	}

	public static TicketPriority map(CustomerType type) {
		TicketPriority priority = TicketPriority.INVALID;
		if (type == CustomerType.VIP) {
			priority = TicketPriority.HIGH;
		} else if (type == CustomerType.LOYAL) {
			priority = TicketPriority.MEDIUM;
		} else if (type == CustomerType.NEW) {
			priority = TicketPriority.LOW;
		}

		return priority;
	}

	public static CustomerType map(TicketPriority priority) {
		CustomerType type = CustomerType.INVALID;
		if (priority == TicketPriority.HIGH) {
			type = CustomerType.VIP;
		} else if (priority == TicketPriority.MEDIUM) {
			type = CustomerType.LOYAL;
		} else if (priority == TicketPriority.LOW) {
			type = CustomerType.NEW;
		}

		return type;
	}
}
