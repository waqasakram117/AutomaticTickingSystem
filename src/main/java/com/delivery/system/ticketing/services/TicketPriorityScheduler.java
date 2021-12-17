package com.delivery.system.ticketing.services;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketPriorityScheduler {

	private final TicketService ticketService;
	private final DeliveryService deliveryService;

	public TicketPriorityScheduler(TicketService ticketService, DeliveryService deliveryService) {
		this.ticketService = ticketService;
		this.deliveryService = deliveryService;
	}

	public void prioritiesTickets() {
		var tickets = ticketService.getPriorityTickets();
		for (Ticket ticket : tickets) {
			var delivery = deliveryService.getDeliveryById(ticket.getDeliveryDbId());
			scheduleTicketForPendingDelivery(delivery);
		}

	}

	private void scheduleTicketForPendingDelivery(Delivery delivery) {
		if (!isOrderDelivered(delivery) && isExpectedDeliveryTimePassed(delivery.getExpectedDeliveryTime())) {
			ticketService.updateTicketLevel(delivery.getId());
		}
	}

	private boolean isExpectedDeliveryTimePassed(LocalDateTime expected) {
		return UtcDateTimeUtils.utcTimeNow().isBefore(expected);
	}

	private boolean isOrderDelivered(Delivery delivery) {
		return delivery.getDeliveryStatus() == DeliveryStatus.DELIEVERED;
	}
}
