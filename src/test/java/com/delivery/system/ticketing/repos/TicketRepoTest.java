package com.delivery.system.ticketing.repos;

import static com.delivery.system.utils.UtcDateTimeUtils.utcTimeNow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=validate"})
@ActiveProfiles("test")
class TicketRepoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private DeliveryRepo deliveryRepo;
	@Autowired
	private TicketRepo ticketRepo;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
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
	void shouldPassToCheckExistenceTicket() {
		var ticket = getFreshPersistedTicket();
		var exist = ticketRepo.existsTicketByDeliveryDbId(ticket.getDeliveryDbId());
		var tickets = ticketRepo.findAll();
		assertThat(tickets.size()).isEqualTo(1);
		assertThat(exist).isTrue();
	}

	@Test
	void shouldSuccessfullyGetPriorityTickets() {
		var count = 10;
		for (int i = 0; i < count; i++) {
			getFreshPersistedTicket();
		}

		var exist = ticketRepo.getPriorityTickets();

		assertThat(exist.size()).isEqualTo(count);
	}

	@Test
	void shouldSuccessfullyUpdateTicketPriorities() {
		var count = 10;
		List<Ticket> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			var ticket = getFreshPersistedTicket();
			list.add(ticket);
		}

		var lowTicketsCount = 5;

		var deliverIds = list.subList(0, lowTicketsCount)
				.stream()
				.map(Ticket::getDeliveryDbId)
				.collect(Collectors.toUnmodifiableList());

		var updatedCount = ticketRepo.updateTicketPriority(deliverIds, TicketPriority.LOW);

		assertThat(lowTicketsCount).isEqualTo(updatedCount);
	}

	private Ticket getFreshPersistedTicket() {
		var deliveryDto = prepareValidDeliveryDTO();
		var delivery = DeliveryMapper.map(deliveryDto);
		delivery.setCreatedAt(utcTimeNow());
		var savedDelivery = deliveryRepo.save(delivery);
		var ticket = TicketMapper.map(savedDelivery.getId(), TicketPriority.HIGH);
		ticket.setCreatedAt(utcTimeNow());
		ticket.setLastModified(utcTimeNow());
		return ticketRepo.saveAndFlush(ticket);
	}


	private NewDeliveryDto prepareValidDeliveryDTO() {

		var now = utcTimeNow();
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