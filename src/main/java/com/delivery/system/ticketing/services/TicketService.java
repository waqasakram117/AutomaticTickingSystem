package com.delivery.system.ticketing.services;

import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.pojos.internal.RegisteredTicketData;
import com.delivery.system.ticketing.repos.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	public void updateTicketPriority(Long deliveryDbId, TicketPriority priority) {
		repo.updateTicketPriority(deliveryDbId, priority);
		log.info("Ticket Priority is updated: {}", deliveryDbId);
	}

	public List<RegisteredTicketData> getPriorityTickets() {

		return repo.getPriorityTickets();
	}

	public void createTicketIfNotExist(Long deliveryId, TicketPriority priority) {
		var exists = repo.existsById(deliveryId);
		if (!exists) {
			createTicket(deliveryId, priority);
			log.info("New Ticket is generated ID: {} ", deliveryId);
		}
	}

	private void createTicket(Long deliveryDbId, TicketPriority priority) {
		repo.save(TicketMapper.map(deliveryDbId, priority));
	}
}
