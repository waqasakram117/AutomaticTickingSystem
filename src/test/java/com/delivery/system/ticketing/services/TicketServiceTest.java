package com.delivery.system.ticketing.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.enums.TicketStatus;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.pojos.internal.RegisteredTicketData;
import com.delivery.system.ticketing.repos.TicketRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@Mock
	private TicketRepo ticketRepo;

	@InjectMocks
	private TicketService ticketService;

	@Test
	void shouldSaveTicketSuccessfully() {
		Ticket ticket = TicketMapper.mapToNewTicket(1L, TicketPriority.HIGH);

		given(ticketRepo.saveAndFlush(ticket)).willReturn(ticket);
		given(ticketRepo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId())).willReturn(Boolean.FALSE);

		var savedTicket = ticketService.createTicketIfNotExist(ticket);
		assertThat(savedTicket).isNotNull();
		verify(ticketRepo).saveAndFlush(any(Ticket.class));
	}

	@Test
	void shouldNotCreateTicketBecauseAlreadyExisted() {
		Ticket ticket = TicketMapper.mapToNewTicket(1L, TicketPriority.HIGH);

		given(ticketRepo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId())).willReturn(Boolean.TRUE);

		Ticket savedUser = ticketService.createTicketIfNotExist(ticket);
		assertThat(savedUser).isNull();
	}

	@Test
	void shouldUpdateTicketsPrioritySuccessfully() {
		var tickets = getTickets()
				.parallelStream()
				.map(Ticket::getDeliveryDbId)
				.collect(Collectors.toUnmodifiableSet());
		var srcSize = tickets.size();

		given(ticketRepo.updateTicketPriorityWhereNotCurrentPriority(anySet(), any(TicketPriority.class),
				any(TicketPriority.class), any(LocalDateTime.class))).willReturn(srcSize);

		var result = ticketService.updateTicketsToHighPriority(tickets);
		assertEquals(srcSize, result);
	}

	@Test
	void successfullyGetPrioritiesTickets() {
		var list = getTickets()
				.parallelStream()
				.map(this::mapToData)
				.collect(Collectors.toUnmodifiableList());

		given(ticketRepo.getPriorityTickets(TicketStatus.OPEN)).willReturn(list);

		var tickets = ticketService.getAllOpenPriorityTickets();

		assertEquals(list, tickets);
	}

	private List<Ticket> getTickets() {
		return List.of(
				TicketMapper.mapToNewTicket(1L, TicketPriority.HIGH),
				TicketMapper.mapToNewTicket(2L, TicketPriority.HIGH),
				TicketMapper.mapToNewTicket(3L, TicketPriority.LOW),
				TicketMapper.mapToNewTicket(4L, TicketPriority.MEDIUM),
				TicketMapper.mapToNewTicket(5L, TicketPriority.MEDIUM));
	}

	private RegisteredTicketData mapToData(Ticket ticket) {
		return new RegisteredTicketData() {
			@Override
			public Long getId() {
				return ticket.getId();
			}

			@Override
			public Long getDeliveryDbId() {
				return ticket.getDeliveryDbId();
			}

			@Override
			public String getPriority() {
				return ticket.getPriority().name();
			}

			@Override
			public LocalDateTime getCreatedAt() {
				return ticket.getCreatedAt();
			}

			@Override
			public LocalDateTime getLastModified() {
				return ticket.getLastModified();
			}
		};
	}


}