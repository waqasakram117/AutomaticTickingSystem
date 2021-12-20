package com.delivery.system.ticketing.repos;

import static com.delivery.system.ticketing.mappers.TicketPriorityMapper.map;
import static com.delivery.system.utils.UtcDateTimeUtils.utcTimeNow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.CustomerType;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=validate"})
@ActiveProfiles("test")
class TicketRepoTest {

	@Autowired
	private DeliveryRepo deliveryRepo;
	@Autowired
	private TicketRepo ticketRepo;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(deliveryRepo).isNotNull();
		assertThat(ticketRepo).isNotNull();
	}

	@Test
	void throwForeignKeyExceptionWithInvalidDeliveryIdForTicketSaving() {
		var ticket = TicketMapper.map(1L, TicketPriority.HIGH);

		assertThatThrownBy(() -> ticketRepo.save(ticket))
				.isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	void shouldSuccessfullySaveTicket() {
		var deliveryDto = prepareValidDeliveryDTO();
		var delivery = deliveryRepo.save(DeliveryMapper.map(deliveryDto));
		var ticket = TicketMapper.map(delivery.getId(), TicketPriority.HIGH);
		var savedTicket = ticketRepo.save(ticket);

		assertThat(savedTicket).isNotNull();
	}

	@Test
	void shouldFailToCheckExistenceTicket() {
		var exist = ticketRepo.existsTicketByDeliveryDbId(1L);
		assertThat(exist).isFalse();
	}

	@Test
	void shouldPassToCheckExistenceOfTicket() {
		var ticket = persistFreshTicket();
		var exist = ticketRepo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId());
		var tickets = ticketRepo.findAll();
		assertThat(tickets.size()).isEqualTo(1);
		assertThat(exist).isTrue();
	}

	@Test
	void shouldSuccessfullyGetPriorityTickets() {

		var count = 5;
		var midTickets = IntStream.range(1, count + 1)
				.mapToObj(i -> persistFreshTicket(TicketPriority.MEDIUM))
				.collect(Collectors.toUnmodifiableList());
		var lowTickets = IntStream.range(1, count + 1)
				.mapToObj(i -> persistFreshTicket(TicketPriority.LOW))
				.collect(Collectors.toUnmodifiableList());
		var highTickets = IntStream.range(1, count + 1)
				.mapToObj(i -> persistFreshTicket(TicketPriority.HIGH))
				.collect(Collectors.toUnmodifiableList());

		var priorityTickets = ticketRepo.getPriorityTickets().stream()
				.map(t -> TicketPriority.valueOf(t.getPriority()))
				.collect(Collectors.toUnmodifiableList());

		var subListHigh = priorityTickets.subList(0, count); // (0, 5)
		var high = subListHigh.stream().allMatch(h -> h == TicketPriority.HIGH);
		var subListMid = priorityTickets.subList(count, count * 2); // (5, 10)
		var mid = subListMid.stream().allMatch(h -> h == TicketPriority.MEDIUM);
		var subListLow = priorityTickets.subList(count * 2, count * 3); // (10, 15)
		var low = subListLow.stream().allMatch(h -> h == TicketPriority.LOW);

		assertThat(high).isTrue();
		assertThat(mid).isTrue();
		assertThat(low).isTrue();
		assertThat(highTickets.size()).isEqualTo(subListHigh.size());
		assertThat(lowTickets.size()).isEqualTo(subListLow.size());
		assertThat(midTickets.size()).isEqualTo(subListMid.size());
	}

	@Test
	void shouldSuccessfullyUpdateTicketPriorities() {
		List<Ticket> list = IntStream.range(1, 10)
				.mapToObj(i -> persistFreshTicket(TicketPriority.LOW))
				.collect(Collectors.toUnmodifiableList());

		var lowTicketsCount = 5;

		var deliverIds = list.subList(0, lowTicketsCount)
				.stream()
				.map(Ticket::getDeliveryDbId)
				.collect(Collectors.toUnmodifiableList());

		var updatedCount = ticketRepo.updateTicketPriority(deliverIds, TicketPriority.LOW);

		assertThat(lowTicketsCount).isEqualTo(updatedCount);
	}


	private Ticket persistFreshTicket() {
		return persistFreshTicket(TicketPriority.MEDIUM);
	}

	private Ticket persistFreshTicket(TicketPriority priority) {
		var deliveryDto = prepareValidDeliveryDTO(map(priority));
		var delivery = DeliveryMapper.map(deliveryDto);
		delivery.setCreatedAt(utcTimeNow());
		var savedDelivery = deliveryRepo.save(delivery);
		var ticket = TicketMapper.map(savedDelivery.getId(), map(delivery.getCustomerType()));
		ticket.setCreatedAt(utcTimeNow());
		ticket.setLastModified(utcTimeNow());
		return ticketRepo.saveAndFlush(ticket);
	}

	private NewDeliveryDto prepareValidDeliveryDTO() {
		return prepareValidDeliveryDTO(CustomerType.NEW);
	}

	private NewDeliveryDto prepareValidDeliveryDTO(CustomerType customerType) {

		var now = utcTimeNow();
		var dto = new NewDeliveryDto();
		dto.setDeliveryStatus("Order received");
		dto.setCustomerType(customerType.type);
		dto.setRiderRating(5);
		dto.setDestinationDistance(5);
		dto.setTimeToReachDestination(now.plusMinutes(30));
		dto.setExpectedDeliveryTime(now.plusMinutes(50));
		dto.setFoodPreparationTime(10);

		return dto;
	}

}