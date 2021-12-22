package com.delivery.system.scheduler.services;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.mappers.TicketPriorityMapper;
import com.delivery.system.ticketing.services.DeliveryService;
import com.delivery.system.ticketing.services.TicketService;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

	public int prioritiesTickets() {

		var deliveries = deliveryService.getAllUndeliveredDeliveries();

		var lateDeliveries = filterLateDeliveriesAndTicketCreated(deliveries);
		var expectedToBeLateDeliveries = filterExpectedTobeLateDeliveriesAndTicketCreated(deliveries);

		handleExpectedToBeLateDeliveries(expectedToBeLateDeliveries);

		return prioritiesLateTickets(lateDeliveries);
	}

	private void handleExpectedToBeLateDeliveries(Map<Boolean, List<Delivery>> toBeLateDeliveries) {

		var newTickets = toBeLateDeliveries.getOrDefault(Boolean.FALSE, Collections.emptyList())
				.stream()
				.map(d -> TicketMapper.mapToNewTicket(d.getId(),
						TicketPriorityMapper.map(d.getCustomerType())))
				.collect(Collectors.toUnmodifiableList());

		var createdTickets = ticketService.createTickets(newTickets);

		log.info("New Tickets {} are created", createdTickets.size());
	}

	private int prioritiesLateTickets(Map<Boolean, List<Delivery>> lateDeliveries) {
		var deliveriesWithTickets = lateDeliveries.getOrDefault(Boolean.TRUE, Collections.emptyList());
		var deliveriesWithoutTickets = lateDeliveries.getOrDefault(Boolean.FALSE, Collections.emptyList());
		var newTickets = deliveriesWithoutTickets.stream()
				.map(d -> TicketMapper.mapToNewTicket(d.getId(), TicketPriority.HIGH))
				.collect(Collectors.toUnmodifiableList());

		ticketService.createTickets(newTickets);

		return ticketService.updateTicketPriority(mapToDeliveryIds(deliveriesWithTickets), TicketPriority.HIGH);
	}

	private Map<Boolean, List<Delivery>> filterLateDeliveriesAndTicketCreated(List<Delivery> deliveries) {

		return deliveries.parallelStream()
				.filter(this::isExpectedDeliveryTimePassed)
				.collect(Collectors.groupingBy(Delivery::getTicketCreated));
	}

	private boolean isExpectedDeliveryTimePassed(Delivery d) {

		return d.getExpectedDeliveryTime().isBefore(UtcDateTimeUtils.utcTimeNow());
	}

	private List<Long> mapToDeliveryIds(List<Delivery> deliveries) {

		return deliveries.stream().map(Delivery::getId).collect(Collectors.toUnmodifiableList());
	}

	private Map<Boolean, List<Delivery>> filterExpectedTobeLateDeliveriesAndTicketCreated(List<Delivery> deliveries) {

		return deliveries.stream().filter(this::isDeliveryGoingToDelay)
				.collect(Collectors.groupingBy(Delivery::getTicketCreated));
	}

	private boolean isDeliveryGoingToDelay(Delivery delivery) {

		return delivery.getExpectedDeliveryTime()
				.isBefore(delivery.getTimeToReachDestination()
						.plusMinutes(delivery.getFoodPreparationTime()));
	}
}
