package com.delivery.system.ticketing.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.delivery.system.exceptions.NotFoundException;
import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.pojos.external.UpdateDeliveryDto;
import com.delivery.system.ticketing.repos.DeliveryRepo;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

	@Mock
	private DeliveryRepo deliveryRepo;

	@Mock
	private TicketService ticketService;

	@InjectMocks
	private DeliveryService deliveryService;


	@Test
	void shouldSaveNewDeliverySuccessfully() {
		var dto = prepareValidDeliveryDTO();
		var delivery = DeliveryMapper.map(dto);
		given(deliveryRepo.saveAndFlush(delivery)).willReturn(delivery);
		var saveDelivery = deliveryService.addNewDelivery(delivery, 5);
		assertThat(saveDelivery).isNotNull();
		verify(deliveryRepo).saveAndFlush(any(Delivery.class));

	}

	@Test
	void shouldSaveNewDeliverySuccessfullyAndCreateTicketAltogether() {
		var dto = prepareValidDeliveryDTO();
		var delivery = DeliveryMapper.map(dto);
		delivery.setId(100L);

		given(deliveryRepo.saveAndFlush(delivery)).willReturn(delivery);
		given(ticketService.createTicketIfNotExist(( any(Ticket.class) ))).willAnswer(invocation -> invocation.getArgument(0));

		var foodTimeExtendReachingTime = dto.getFoodPreparationTime() * 10000;
		var savedDelivery = deliveryService.addNewDelivery(delivery, foodTimeExtendReachingTime);

		assertThat(savedDelivery).isNotNull();
		verify(deliveryRepo).saveAndFlush(any(Delivery.class));

	}

	@Test()
	void shouldSuccessfullyReturnedDeliveryById() {
		var dto = prepareValidDeliveryDTO();
		var delivery = DeliveryMapper.map(dto);
		given(deliveryRepo.findById(any(Long.class))).willReturn(Optional.of(delivery));

		var savedDelivery = deliveryService.getDeliveryById(232L);
		assertThat(savedDelivery).isNotNull();
	}

	@Test()
	void throwNotFoundExceptionForUnKnownDeliveryById() {
		var id = 12312L;
		given(deliveryRepo.findById(id)).willReturn(Optional.empty());

		assertThatThrownBy(() -> deliveryService.getDeliveryById(id))
				.isInstanceOf(NotFoundException.class)
				.hasMessageStartingWith("Delivery doesn't exist");
	}

	@Test
	void shouldSuccessfullyUpdateDelivery() {
		var updateDto = prepareValidUpdateDeliveryDTO();
		updateDto.setDeliveryStatus(DeliveryStatus.DELIEVERED.status);
		var dto = prepareValidDeliveryDTO();
		var delivery = DeliveryMapper.map(dto);
		var lastModifiedTime = delivery.getLastModified();

		given(deliveryRepo.findById(any(Long.class))).willReturn(Optional.of(delivery));
		given(deliveryRepo.saveAndFlush(delivery)).willReturn(delivery);

		var updatedDelivery = deliveryService.updateDelivery(updateDto);

		assertThat(lastModifiedTime).isNotEqualTo(updatedDelivery.lastModified);
		assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIEVERED);
	}


	@Test
	void shouldSuccessfullyUpdateDeliveryAndCreateTicket() {
		var updateDto = prepareValidUpdateDeliveryDTO();
		var dto = prepareValidDeliveryDTO();
		var delivery = DeliveryMapper.map(dto);
		var lastModifiedTime = delivery.getLastModified();
		updateDto.setFoodPreparationTime(updateDto.getFoodPreparationTime() * 10000);

		given(deliveryRepo.findById(any(Long.class))).will(invocation -> {
			delivery.setId(invocation.getArgument(0));

			return Optional.of(delivery);
		});
		given(deliveryRepo.saveAndFlush(delivery)).willReturn(delivery);
		given(ticketService.createTicketIfNotExist(( any(Ticket.class) ))).willAnswer(invocation -> invocation.getArgument(0));

		var updatedDelivery = deliveryService.updateDelivery(updateDto);

		assertThat(lastModifiedTime).isNotEqualTo(updatedDelivery.lastModified);
		assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.PREPARING);
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

	private UpdateDeliveryDto prepareValidUpdateDeliveryDTO() {
		var now = UtcDateTimeUtils.utcTimeNow();
		var dto = new UpdateDeliveryDto();
		dto.setDeliveryId(101L);
		dto.setDeliveryStatus("Order Preparing");
		dto.setTimeToReachDestination(now.plusMinutes(30));
		dto.setFoodPreparationTime(10);

		return dto;
	}
}