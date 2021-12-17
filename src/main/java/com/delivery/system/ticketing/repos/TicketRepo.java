package com.delivery.system.ticketing.repos;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {


	@Modifying
	@Query("update Ticket ticket " +
			" set ticket.priority=:priority, " +
			" ticket.lastModified= current_timestamp " +
			" where ticket.deliveryDbId =:deliveryDbId ")
	@Transactional(propagation = Propagation.MANDATORY)
	void updateTicketPriority(@Param("deliveryDbId") Long deliveryDbId, @Param("priority") TicketPriority priority);

	@Modifying
	@Query("update Ticket ticket " +
			" set ticket.lastModified= current_timestamp " +
			" where ticket.deliveryDbId =:deliveryDbId ")
	@Transactional(propagation = Propagation.MANDATORY)
	void updateTicket(@Param("deliveryDbId") Long deliveryDbId);

	@Query("select ticket from Ticket ticket order by ticket.priority asc, ticket.lastModified desc")
	List<Ticket> getPriorityTickets();

}
