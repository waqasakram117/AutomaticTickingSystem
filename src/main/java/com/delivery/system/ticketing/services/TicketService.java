package com.delivery.system.ticketing.services;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.pojos.internal.RegisteredTicketData;
import com.delivery.system.ticketing.repos.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

	private static final Logger log = LoggerFactory.getLogger(TicketService.class);
	private final TicketRepo repo;

	@Autowired
	public TicketService(TicketRepo repo) {
		this.repo = repo;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public long updateTicketPriority(Long deliveryDbId, TicketPriority priority) {
		var results = repo.updateTicketPriority(deliveryDbId, priority);
		log.info("Ticket Priority is updated: {}", deliveryDbId);

		return results;
	}

	public List<RegisteredTicketData> getPriorityTickets() {

		return repo.getPriorityTickets();
	}

	@Nullable
	public Ticket createTicketIfNotExist(Ticket ticket) {

		return repo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId()) ? null :
				createTicket(ticket);
	}

	private Ticket createTicket(Ticket ticket) {
		var savedTicket = repo.save(ticket);
		log.info("New Ticket is generated ID: {} ", savedTicket);

		return savedTicket;
	}
}
