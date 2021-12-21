package com.delivery.system.ticketing.services;

import static com.delivery.system.utils.UtcDateTimeUtils.utcTimeNow;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.pojos.internal.RegisteredTicketData;
import com.delivery.system.ticketing.repos.TicketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TicketService {

	private static final Logger log = LoggerFactory.getLogger(TicketService.class);
	private final TicketRepo repo;

	public TicketService(TicketRepo repo) {
		this.repo = repo;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updateTicketPriority(List<Long> deliveryIds, TicketPriority priority) {
		var results = repo.updateTicketPriority(deliveryIds, priority, utcTimeNow());
		var logMsg = Arrays.toString(deliveryIds.toArray());
		log.info("Total {} Tickets Priorities are updated: {}", results, logMsg);

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
		log.info("New Ticket is generated ID: {} ", savedTicket.getDeliveryDbId());

		return savedTicket;
	}
}
