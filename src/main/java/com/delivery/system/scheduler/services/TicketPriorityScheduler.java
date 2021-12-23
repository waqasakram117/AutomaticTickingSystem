package com.delivery.system.scheduler.services;

import static com.delivery.system.ticketing.mappers.TicketMapper.mapToNewTicket;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.services.DeliveryService;
import com.delivery.system.ticketing.services.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketPriorityScheduler {

	private final TicketService ticketService;
	private final DeliveryService deliveryService;


	public TicketPriorityScheduler(TicketService ticketService, DeliveryService deliveryService) {
		this.ticketService = ticketService;
		this.deliveryService = deliveryService;
	}

	/**
	 * Complete Algo to prioritise tickets:
	 * 1: Get All undelivered late deliveries (Expected Time of deliveries has passed)
	 * 2: Get All open tickets against those deliveries
	 * 3: Filter deliveries Ids those don't have tickets
	 * 4: Priorities Tickets {@link #prioritiesLateTickets(Set, Set)}
	 *
	 * @return total number of tickets that status changes to HIGH priority.
	 */

	public int prioritiesTickets() {
		var lateDeliveries = deliveryService.getAllUndeliveredLateDeliveries();
		var deliverIds = getDeliverIds(lateDeliveries);
		var deliveriesTickets = ticketService.getAllOpenTicketsByDeliveries(deliverIds);
		var deliveriesWithoutTickets = filterAllDeliveriesWithoutTickets(deliverIds, deliveriesTickets);

		return prioritiesLateTickets(deliverIds, deliveriesWithoutTickets);
	}

	/**
	 * @param lateDeliveries           Ids of those deliveries are real time late to be delivered
	 * @param deliveriesWithoutTickets Those deliveries don't have tickets to associate with them
	 *                                 1: Create new HIGH priority tickets for deliveries those don't have tickets before
	 *                                 2: Update tickets to HIGH priority for deliveries that already have tickets
	 * @return total number of tickets their priorities set to HIGH.
	 */

	private int prioritiesLateTickets(Set<Long> lateDeliveries, Set<Long> deliveriesWithoutTickets) {
		var deliveriesByTickets = lateDeliveries
				.stream()
				.collect(Collectors.groupingBy(deliveriesWithoutTickets::contains, toSet()));
		var ticketsToCreate = deliveriesByTickets.getOrDefault(Boolean.TRUE, emptySet());
		var ticketsForUpdate = deliveriesByTickets.getOrDefault(Boolean.FALSE, emptySet());
		var newTickets = ticketsToCreate
				.stream()
				.map(d -> mapToNewTicket(d, TicketPriority.HIGH))
				.collect(Collectors.toUnmodifiableList());

		var newlyCreatedTickets = ticketService.createTickets(newTickets);
		var updatedTickets = ticketService.updateTicketsToHighPriority(ticketsForUpdate);

		return newlyCreatedTickets.size() + updatedTickets;
	}

	private Set<Long> getDeliverIds(List<Delivery> deliveries) {
		return deliveries.stream().map(Delivery::getId).collect(Collectors.toUnmodifiableSet());
	}

	private Set<Long> filterAllDeliveriesWithoutTickets(Set<Long> srcDeliveryIds, List<Ticket> tickets) {
		var tempSet = srcDeliveryIds.parallelStream().collect(toSet());
		var ticketDeliveryIds = tickets.parallelStream().map(Ticket::getDeliveryDbId).collect(toSet());

		tempSet.removeAll(ticketDeliveryIds);

		return tempSet;
	}
}
