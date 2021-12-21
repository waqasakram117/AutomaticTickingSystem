package com.delivery.system.scheduler.services;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.services.DeliveryService;
import com.delivery.system.ticketing.services.TicketService;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketPriorityScheduler {

	private static final Logger log = LoggerFactory.getLogger(TicketPriorityScheduler.class);
	private final TicketService ticketService;
	private final DeliveryService deliveryService;


	public TicketPriorityScheduler(TicketService ticketService, DeliveryService deliveryService) {
		this.ticketService = ticketService;
		this.deliveryService = deliveryService;
	}

	public int prioritiesTickets(LocalDateTime from) {

		var deliveries = deliveryService.getAllDeliveries(from);
		log.info("Current syncing from time {}, results: [{}]", from, deliveries.size());

		var pendingDeliveries = filterPendingDeliveries(deliveries);

		return ticketService.updateTicketPriority(pendingDeliveries, TicketPriority.HIGH);
	}


	private List<Long> filterPendingDeliveries(List<Delivery> deliveries) {

		return deliveries.parallelStream()
				.filter(this::isDeliveryPending)
				.map(Delivery::getId)
				.collect(Collectors.toUnmodifiableList());
	}

	private boolean isDeliveryPending(Delivery d) {
		return !isOrderDelivered(d) && isExpectedDeliveryTimePassed(d.getExpectedDeliveryTime());
	}

	private boolean isExpectedDeliveryTimePassed(LocalDateTime expected) {
		return expected.isBefore(UtcDateTimeUtils.utcTimeNow());
	}

	private boolean isOrderDelivered(Delivery delivery) {
		return delivery.getDeliveryStatus() == DeliveryStatus.DELIEVERED;
	}
}
