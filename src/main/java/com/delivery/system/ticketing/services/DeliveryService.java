package com.delivery.system.ticketing.services;

import static com.delivery.system.ticketing.mappers.DeliveryMapper.mapToRegisteredData;
import static com.delivery.system.utils.UtcDateTimeUtils.utcTimeNow;

import com.delivery.system.exceptions.NotFoundException;
import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.mappers.TicketPriorityMapper;
import com.delivery.system.ticketing.pojos.external.UpdateDeliveryDto;
import com.delivery.system.ticketing.pojos.internal.RegisteredDeliveryData;
import com.delivery.system.ticketing.repos.DeliveryRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

	private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);
	private final DeliveryRepo deliveryRepo;
	private final TicketService ticketService;

	public DeliveryService(DeliveryRepo deliveryRepo, TicketService ticketService) {
		this.deliveryRepo = deliveryRepo;
		this.ticketService = ticketService;
	}

	public RegisteredDeliveryData addNewDelivery(Delivery newDelivery, int foodPreparationTime) {
		var delivery = deliveryRepo.saveAndFlush(newDelivery);
		log.info("New delivery is created. ID: {}", delivery.getId());
		scheduleTicketForLateDelivery(delivery, delivery.getTimeToReachDestination(), foodPreparationTime);

		return mapToRegisteredData(delivery);
	}

	public RegisteredDeliveryData updateDelivery(UpdateDeliveryDto dto) {
		var delivery = getDeliveryById(dto.getDeliveryId());
		updateDeliveryStatus(delivery, dto.getDeliveryStatus());
		updateFoodPreparationTime(delivery, dto.getFoodPreparationTime());
		updateDistanceFromDestination(delivery, dto.getDistanceFromDestination());
		analysisEstimationTime(delivery, dto);
		delivery.setLastModified(utcTimeNow());

		var updatedDelivery = deliveryRepo.saveAndFlush(delivery);
		log.info("Delivery ID: {} is updated", updatedDelivery.getId());

		return mapToRegisteredData(updatedDelivery);
	}

	private void updateFoodPreparationTime(Delivery delivery, Integer foodPreparationTime) {
		if (foodPreparationTime != null) {
			delivery.setFoodPreparationTime(foodPreparationTime);
		}
	}

	public List<Delivery> getAllUndeliveredLateDeliveries() {
		return deliveryRepo.findAllDeliveriesByNotStatusAndCurrentTimeIsMoreThanExpected(
				DeliveryStatus.DELIEVERED, utcTimeNow());
	}


	private void analysisEstimationTime(Delivery delivery, UpdateDeliveryDto dto) {
		LocalDateTime reachingTime = dto.getTimeToReachDestination();
		var foodPrepare = dto.getFoodPreparationTime();
		updateValidReachingTime(delivery, reachingTime);
		scheduleTicketForLateDelivery(delivery, reachingTime, foodPrepare);
	}

	private void updateValidReachingTime(Delivery delivery, LocalDateTime reachingTime) {
		if (reachingTime != null) {
			delivery.setTimeToReachDestination(reachingTime);
		}
	}

	private void updateDistanceFromDestination(Delivery delivery, Integer distance) {
		if (distance != null) {
			delivery.setDestinationDistance(distance);
		}
	}

	private void scheduleTicketForLateDelivery(Delivery delivery, LocalDateTime reachTime, Integer foodPreparationTime) {
		if (isDeliveryGoingToDelay(
				delivery.getExpectedDeliveryTime(),
				reachTime == null ? delivery.getTimeToReachDestination() : reachTime,
				foodPreparationTime == null ? 0 : foodPreparationTime)) {
			scheduleTicket(delivery);
		}
	}

	private boolean isDeliveryGoingToDelay(LocalDateTime expectedDeliveryTime, LocalDateTime reachTime, int foodPreparationTime) {
		return expectedDeliveryTime.isBefore(reachTime.plusMinutes(foodPreparationTime));
	}

	private void scheduleTicket(Delivery delivery) {
		ticketService.createTicketIfNotExist(TicketMapper.mapToNewTicket(delivery.getId(),
				TicketPriorityMapper.map(delivery.getCustomerType())));
	}

	private void updateDeliveryStatus(Delivery delivery, String status) {
		if (status != null) {
			var deliveryStatus = DeliveryStatus.getByStatus(status);
			delivery.setDeliveryStatus(deliveryStatus);
			if (deliveryStatus == DeliveryStatus.DELIEVERED) {
				ticketService.closeTicket(delivery.getId());
			}
		}
	}

	public Delivery getDeliveryById(Long deliveryId) {

		return deliveryRepo.findById(deliveryId).orElseThrow(() -> new NotFoundException("Delivery doesn't exist against Id: " + deliveryId));
	}
}
