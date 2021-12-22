package com.delivery.system.ticketing.repos;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.enums.TicketStatus;
import com.delivery.system.ticketing.pojos.internal.RegisteredTicketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

	boolean existsTicketByDeliveryDbId(long deliveryId);

	@Modifying
	@Query("update Ticket ticket " +
			" set ticket.priority =:priority, " +
			" ticket.lastModified =:lastModifiedDate " +
			" where ticket.deliveryDbId in :deliveryIds ")
	@Transactional(propagation = Propagation.MANDATORY)
	int updateTicketPriority(@Param("deliveryIds") Set<Long> deliveryIds,
	                         @Param("priority") TicketPriority priority,
	                         @Param("lastModifiedDate") LocalDateTime lastModifiedDate);

	@Query("select ticket from Ticket ticket order by ticket.priority asc, ticket.lastModified desc")
	List<RegisteredTicketData> getPriorityTickets();

	@Query("select ticket from Ticket ticket " +
			"where ticket.deliveryDbId in :deliveryIds" +
			" and ticket.ticketStatus =:status")
	List<Ticket> getAllTicketsByDeliveryDbIdsAndStatus(@Param("deliveryIds") Set<Long> deliveryIds,
	                                                   @Param("status") TicketStatus status);

}
