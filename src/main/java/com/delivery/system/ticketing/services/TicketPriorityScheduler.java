package com.delivery.system.ticketing.services;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketPriorityScheduler {

	private final TicketService ticketService;
	private final DeliveryService deliveryService;

	@Autowired
	public TicketPriorityScheduler(TicketService ticketService, DeliveryService deliveryService) {
		this.ticketService = ticketService;
		this.deliveryService = deliveryService;
	}

	public void prioritiesTickets(LocalDateTime from, LocalDateTime to) {
//		var tickets = ticketService.getPriorityTickets();
//		for (Ticket ticket : tickets) {
//			var delivery = deliveryService.getDeliveryById(ticket.getDeliveryDbId());
//			scheduleTicketForPendingDelivery(delivery);
//		}

		System.out.println("Last sync time " + from);
		System.out.println("To sync time " + to);
		var deliveries = deliveryService.getAllDeliveries(from, to);

		for (Delivery delivery : deliveries) {
			System.out.printf("ID: %d, time: %s priority: %s %n", delivery.getId(),
					delivery.getLastModified(),
					delivery.getDeliveryStatus().name()
			);
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
