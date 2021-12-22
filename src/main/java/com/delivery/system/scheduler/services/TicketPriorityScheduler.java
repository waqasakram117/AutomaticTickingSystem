package com.delivery.system.scheduler.services;

import static com.delivery.system.ticketing.mappers.TicketMapper.mapToNewTicket;
import static com.delivery.system.ticketing.mappers.TicketPriorityMapper.map;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.services.DeliveryService;
import com.delivery.system.ticketing.services.TicketService;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
		var deliveryByIds = mapToDeliveryIds(deliveries);
		var ids = deliveryByIds.keySet();

		var deliveriesTickets = ticketService.getAllOpenTicketsByDeliveries(ids);

		var deliveriesWithoutTickets = filterAllDeliveriesWithoutTickets(ids, deliveriesTickets);
		var deliveriesWithTickets = filterAllDeliveriesWithTickets(ids, deliveriesTickets);

		var lateDeliveries = filterLateDeliveriesAndTicketCreated(deliveries);
		var expectedToBeLateDeliveries = filterExpectedTobeLateDeliveriesAndTicketCreated(deliveries);

		handleExpectedToBeLateDeliveries(deliveryByIds, deliveriesWithoutTickets, expectedToBeLateDeliveries);

		return prioritiesLateTickets(lateDeliveries, deliveriesWithoutTickets, deliveriesWithTickets);
	}

	private void handleExpectedToBeLateDeliveries(Map<Long, Delivery> deliveryByIds, Set<Long> deliveriesWithoutTickets,
	                                              Set<Long> expectedToBeLateDeliveries) {

		var newTickets = deliveriesWithoutTickets
				.stream()
				.filter(expectedToBeLateDeliveries::contains)
				.map(i -> {
					var d = deliveryByIds.get(i);
					return mapToNewTicket(d.getId(), map(d.getCustomerType()));
				}).collect(Collectors.toUnmodifiableList());

		var createdTickets = ticketService.createTickets(newTickets);

		log.info("New Tickets {} are created", createdTickets.size());
	}

	private int prioritiesLateTickets(Set<Long> lateDeliveries,
	                                  Set<Long> deliveriesWithoutTickets, Set<Long> deliveriesWithTickets) {


		var ticketsForUpdate = lateDeliveries.stream().filter(deliveriesWithTickets::contains).collect(Collectors.toSet());
		var ticketsToCreate = lateDeliveries.stream().filter(deliveriesWithoutTickets::contains).collect(Collectors.toSet());
		var newTickets = ticketsToCreate.stream()
				.map(d -> mapToNewTicket(d, TicketPriority.HIGH))
				.collect(Collectors.toUnmodifiableList());

		ticketService.createTickets(newTickets);

		return ticketService.updateTicketPriority(ticketsForUpdate, TicketPriority.HIGH);
	}

	private Set<Long> filterLateDeliveriesAndTicketCreated(List<Delivery> deliveries) {

		return deliveries.parallelStream()
				.filter(this::isExpectedDeliveryTimePassed)
				.map(Delivery::getId)
				.collect(Collectors.toUnmodifiableSet());
	}

	private boolean isExpectedDeliveryTimePassed(Delivery d) {

		return d.getExpectedDeliveryTime().isBefore(UtcDateTimeUtils.utcTimeNow());
	}

	private Map<Long, Delivery> mapToDeliveryIds(List<Delivery> deliveries) {

		return deliveries.stream().collect(Collectors.toUnmodifiableMap(Delivery::getId, Function.identity()));
	}

	private Set<Long> filterExpectedTobeLateDeliveriesAndTicketCreated(List<Delivery> deliveries) {

		return deliveries.stream().filter(this::isDeliveryGoingToDelay)
				.map(Delivery::getId)
				.collect(Collectors.toSet());
	}

	private boolean isDeliveryGoingToDelay(Delivery delivery) {

		return delivery.getExpectedDeliveryTime()
				.isBefore(delivery.getTimeToReachDestination()
						.plusMinutes(delivery.getFoodPreparationTime()));
	}

	private Set<Long> filterAllDeliveriesWithoutTickets(Set<Long> srcDeliveryIds, List<Ticket> tickets) {
		var tempSet = srcDeliveryIds.parallelStream().collect(Collectors.toSet());
		var ticketDeliveryIds = tickets.parallelStream().map(Ticket::getDeliveryDbId).collect(Collectors.toSet());

		tempSet.removeAll(ticketDeliveryIds);

		return tempSet;
	}

	private Set<Long> filterAllDeliveriesWithTickets(Set<Long> srcDeliveryIds, List<Ticket> tickets) {

		return tickets.parallelStream()
				.map(Ticket::getDeliveryDbId)
				.filter(srcDeliveryIds::contains)
				.collect(Collectors.toSet());
	}
}
