package com.delivery.system.ticketing.mappers;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.enums.TicketStatus;

public final class TicketMapper {

	private TicketMapper() {
	}

	public static Ticket mapToNewTicket(Long deliveryDbId, TicketPriority priority) {
		var ticket = new Ticket();
		ticket.setPriority(priority);
		ticket.setDeliveryDbId(deliveryDbId);
		ticket.setTicketStatus(TicketStatus.OPEN);

		return ticket;
	}

	public static Ticket mapToNewTicket(Delivery delivery) {
		var ticket = new Ticket();
		ticket.setPriority(TicketPriorityMapper.map(delivery.getCustomerType()));
		ticket.setDeliveryDbId(delivery.getId());
		ticket.setTicketStatus(TicketStatus.OPEN);

		return ticket;
	}
}
