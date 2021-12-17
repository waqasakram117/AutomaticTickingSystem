package com.delivery.system.ticketing.mappers;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;

public final class TicketMapper {

	private TicketMapper() {
	}

	public static Ticket map(Long deliveryDbId, TicketPriority priority) {
		var ticket = new Ticket();
		ticket.setPriority(priority);
		ticket.setDeliveryDbId(deliveryDbId);

		return ticket;
	}
}
