package com.delivery.system.ticketing.services;

import static com.delivery.system.utils.UtcDateTimeUtils.utcTimeNow;
import static java.util.Collections.emptyList;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.enums.TicketStatus;
import com.delivery.system.ticketing.pojos.internal.RegisteredTicketData;
import com.delivery.system.ticketing.repos.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TicketService {

	private static final Logger log = LoggerFactory.getLogger(TicketService.class);
	private final TicketRepo repo;

	public TicketService(TicketRepo repo) {
		this.repo = repo;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updateTicketsToHighPriority(Set<Long> deliveryIds) {
		if (deliveryIds.isEmpty()) return 0;

		var results = repo.updateTicketPriorityWhereNotCurrentPriority(deliveryIds,
				TicketPriority.HIGH, TicketPriority.HIGH, utcTimeNow());
		log.info("Total {} Tickets Priorities are updated", results);

		return results;
	}

	public List<RegisteredTicketData> getAllOpenPriorityTickets() {

		return repo.getPriorityTickets(TicketStatus.OPEN);
	}

	public List<Ticket> getAllOpenTicketsByDeliveries(Set<Long> deliveries) {

		return repo.getAllTicketsByDeliveryDbIdsAndStatus(deliveries, TicketStatus.OPEN);
	}

	public List<Ticket> createTickets(List<Ticket> tickets) {
		if (tickets.isEmpty()) return emptyList();

		var createdTickets = repo.saveAllAndFlush(tickets);
		log.info("New Tickets {} are created", createdTickets.size());

		return createdTickets;
	}

	@Nullable
	public Ticket createTicketIfNotExist(Ticket ticket) {

		return repo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId()) ? null :
				createTicket(ticket);
	}

	private Ticket createTicket(Ticket ticket) {
		var savedTicket = repo.saveAndFlush(ticket);
		log.info("New Ticket is generated ID: {} ", savedTicket.getDeliveryDbId());

		return savedTicket;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void closeTicket(Long deliveryId) {
		var result = repo.updateTicketStatusByDeliveryId(deliveryId, TicketStatus.CLOSE);
		log.info("Ticket Closed against delivery ID: {}, results {}", deliveryId, result);

	}
}
