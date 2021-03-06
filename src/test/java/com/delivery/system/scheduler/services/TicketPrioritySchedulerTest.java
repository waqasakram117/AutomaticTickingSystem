package com.delivery.system.scheduler.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.services.DeliveryService;
import com.delivery.system.ticketing.services.TicketService;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
class TicketPrioritySchedulerTest {

	@Mock
	TicketService ticketService;

	@Mock
	DeliveryService deliveryService;

	@InjectMocks
	TicketPriorityScheduler scheduler;


	@SuppressWarnings("unchecked")
	@Test
	void shouldSuccessfullyCreateTicketAndScheduleHighPriority() {
		var lateDeliveries = prepareValidDeliveries(11, 21)
				.stream()
				.peek(d -> d.setExpectedDeliveryTime(UtcDateTimeUtils.utcTimeNow().minusMinutes(10)))
				.collect(Collectors.toUnmodifiableList());

		given(deliveryService.getAllUndeliveredLateDeliveries()).willReturn(lateDeliveries);
		given(ticketService.getAllOpenTicketsByDeliveries(anySet()))
				.willAnswer(invocation -> Collections.emptyList());
		given(ticketService.updateTicketsToHighPriority(anySet()))
				.willAnswer(invocation -> ( (Set<Long>) invocation.getArgument(0) ).size());
		given(ticketService.createTickets(anyList())).willAnswer(invocation -> invocation.getArgument(0));
		given(ticketService.updateTicketsToHighPriority(anySet()))
				.willAnswer(invocation -> ( (Set<Long>) invocation.getArgument(0) ).size());

		var result = scheduler.prioritiesTickets();

		assertThat(result).isEqualTo(10);
	}

	@SuppressWarnings("unchecked")
	@Test
	void shouldSuccessfullyNotPrioritiesDeliveredDeliveries() {

		var lateDeliveries = prepareValidDeliveries(11, 21)
				.stream()
				.peek(d -> {
					d.setExpectedDeliveryTime(UtcDateTimeUtils.utcTimeNow().minusMinutes(10));
					d.setDeliveryStatus(DeliveryStatus.DELIEVERED);
				})
				.collect(Collectors.toUnmodifiableList());

		given(deliveryService.getAllUndeliveredLateDeliveries()).willReturn(Collections.emptyList());
		given(ticketService.getAllOpenTicketsByDeliveries(anySet()))
				.willAnswer(invocation -> Collections.emptyList());
		given(ticketService.updateTicketsToHighPriority(anySet()))
				.willAnswer(invocation -> ( (Set<Long>) invocation.getArgument(0) ).size());
		given(ticketService.createTickets(anyList())).willAnswer(invocation -> invocation.getArgument(0));
		given(ticketService.updateTicketsToHighPriority(anySet()))
				.willAnswer(invocation -> ( (Set<Long>) invocation.getArgument(0) ).size());

		var result = scheduler.prioritiesTickets();

		assertThat(result).isZero();//because all are delivered
	}


	private List<Delivery> prepareValidDeliveries(int start, int end) {
		return IntStream.range(start, end).mapToObj(i -> {
			var d = DeliveryMapper.map(prepareValidDeliveryDTO());
			d.setId((long) i);
			return d;
		}).collect(Collectors.toUnmodifiableList());
	}


	private NewDeliveryDto prepareValidDeliveryDTO() {

		var now = UtcDateTimeUtils.utcTimeNow();
		var dto = new NewDeliveryDto();
		dto.setDeliveryStatus("Order received");
		dto.setCustomerType("New");
		dto.setRiderRating(5);
		dto.setDestinationDistance(5);
		dto.setTimeToReachDestination(now.plusMinutes(30));
		dto.setExpectedDeliveryTime(now.plusMinutes(50));
		dto.setFoodPreparationTime(10);

		return dto;
	}

}