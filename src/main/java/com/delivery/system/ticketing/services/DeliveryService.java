package com.delivery.system.ticketing.services;

import com.delivery.system.exceptions.NotFoundException;
import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.pojos.external.UpdateDeliveryDto;
import com.delivery.system.ticketing.repos.DeliveryRepo;
import com.delivery.system.ticketing.validation.ValidAheadTime;
import com.delivery.system.ticketing.validation.ValidDeliveryStatus;
import com.delivery.system.utils.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

	private final DeliveryRepo repo;

	@Autowired
	public DeliveryService(DeliveryRepo repo) {
		this.repo = repo;
	}

	public void addNewDelivery(NewDeliveryDto dto) {
		repo.saveAndFlush(DeliveryMapper.map(dto));
	}

	public void updateDelivery(UpdateDeliveryDto dto) {
		var delivery = getDeliveryById(dto.getDeliveryId());
		updateDeliveryStatus(delivery, dto.getDeliveryStatus());
		analysisEstimationTime(delivery, dto);

		repo.saveAndFlush(delivery);
	}

	public List<Delivery> getAllDeliveries(LocalDateTime from, LocalDateTime to) {
		return repo.findAllWithLastModifiedAfter(from, to);
	}

	private void analysisEstimationTime(Delivery delivery, UpdateDeliveryDto dto) {
		LocalDateTime reachingTime = dto.getTimeToReachDestination();

		if (reachingTime != null) {
			BeanValidator.validate(reachingTime, ValidAheadTime.class);
			delivery.setTimeToReachDestination(reachingTime);
		}

		if (dto.getFoodPreparationTime() != null && reachingTime != null) {
			// TODO update ticket
		} else if (dto.getFoodPreparationTime() != null) {
			// TODO GET reach destination time from delivery and look at ticket
		} else if (reachingTime != null) {
			// TODO GET assume food prepared but late in delievery
		}
	}

	private void scheduleTicket(LocalDateTime expectedTime, LocalDateTime reachTime, Long foodPreparationTime) {
		if (expectedTime.isBefore(reachTime.plusSeconds(foodPreparationTime))) {
			// TODO create or update tickets
		}
	}

	private void updateDeliveryStatus(Delivery delivery, DeliveryStatus status) {
		if (status != null) {
			BeanValidator.validate(status, ValidDeliveryStatus.class);
			delivery.setDeliveryStatus(status);
		}
	}

	public Delivery getDeliveryById(Long deliveryId) {

		return repo.findById(deliveryId).orElseThrow(() -> new NotFoundException("Delivery doesn't exist against Id: " + deliveryId));
	}
}
