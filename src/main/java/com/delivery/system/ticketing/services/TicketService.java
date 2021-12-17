package com.delivery.system.ticketing.services;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.repos.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

	private final TicketRepo repo;

	@Autowired
	public TicketService(TicketRepo repo) {
		this.repo = repo;
	}

	public void createTicket(Long deliveryDbId, TicketPriority priority) {
		repo.save(TicketMapper.map(deliveryDbId, priority));
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateTicketPriority(Long deliveryDbId, TicketPriority priority) {
		repo.updateTicketPriority(deliveryDbId, priority);
	}

	public List<Ticket> getPriorityTickets() {
		return repo.getPriorityTickets();
	}
}
