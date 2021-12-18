package com.delivery.system.ticketing.services;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketPriorityScheduler {

	private static final Logger log = LoggerFactory.getLogger(TicketPriorityScheduler.class);
	private final TicketService ticketService;
	private final DeliveryService deliveryService;


	@Autowired
	public TicketPriorityScheduler(TicketService ticketService, DeliveryService deliveryService) {
		this.ticketService = ticketService;
		this.deliveryService = deliveryService;
	}

	public void prioritiesTickets(LocalDateTime from, LocalDateTime to) {
		var deliveries = deliveryService.getAllDeliveries(from, to);

		System.out.printf("Now: %s Last: %s Size: %d %n", from, to, deliveries.size());
		for (Delivery delivery : deliveries) {
			prioritiesTicketForPendingDelivery(delivery);
		}

	}

	private void prioritiesTicketForPendingDelivery(Delivery delivery) {
		if (!isOrderDelivered(delivery) && isExpectedDeliveryTimePassed(delivery.getExpectedDeliveryTime())) {
			ticketService.updateTicketPriority(delivery.getId(), TicketPriority.HIGH);
			log.info("Delivery: {} priority is changed", delivery.getId());
		}
	}

	private boolean isExpectedDeliveryTimePassed(LocalDateTime expected) {
		return UtcDateTimeUtils.utcTimeNow().isBefore(expected);
	}

	private boolean isOrderDelivered(Delivery delivery) {
		return delivery.getDeliveryStatus() == DeliveryStatus.DELIEVERED;
	}
}
