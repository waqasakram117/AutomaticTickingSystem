package com.delivery.system.ticketing.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
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
		Ticket ticket = TicketMapper.map(1L, TicketPriority.HIGH);

		given(ticketRepo.save(ticket)).willReturn(ticket);
		given(ticketRepo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId())).willReturn(Boolean.FALSE);

		Ticket savedUser = ticketService.createTicketIfNotExist(ticket);
		assertThat(savedUser).isNotNull();
		verify(ticketRepo).save(any(Ticket.class));
	}

	@Test
	void shouldNotCreateTicketBecauseAlreadyExisted() {
		Ticket ticket = TicketMapper.map(1L, TicketPriority.HIGH);

		given(ticketRepo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId())).willReturn(Boolean.TRUE);

		Ticket savedUser = ticketService.createTicketIfNotExist(ticket);
		assertThat(savedUser).isNull();
	}

	@Test
	void shouldUpdateTicketPrioritySuccessfully() {
		var tickets = getTickets()
				.parallelStream()
				.map(Ticket::getDeliveryDbId)
				.collect(Collectors.toUnmodifiableList());
		var srcSize = tickets.size();

		given(ticketRepo.updateTicketPriority(tickets, TicketPriority.LOW)).willReturn(srcSize);

		var result = ticketService.updateTicketPriority(tickets, TicketPriority.LOW);
		assertEquals(srcSize, result);
	}

	@Test
	void successfullyGetPrioritiesTickets() {
		var list = getTickets().stream()
				.map(this::mapToData).collect(Collectors.toUnmodifiableList());

		given(ticketRepo.getPriorityTickets()).willReturn(list);

		var tickets = ticketService.getPriorityTickets();

		assertEquals(list, tickets);
	}

	private List<Ticket> getTickets() {
		return List.of(
				TicketMapper.map(1L, TicketPriority.HIGH),
				TicketMapper.map(2L, TicketPriority.HIGH),
				TicketMapper.map(3L, TicketPriority.LOW),
				TicketMapper.map(4L, TicketPriority.MEDIUM),
				TicketMapper.map(5L, TicketPriority.MEDIUM));
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