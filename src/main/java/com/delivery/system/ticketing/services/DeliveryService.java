package com.delivery.system.ticketing.services;

import com.delivery.system.exceptions.NotFoundException;
import com.delivery.system.ticketing.entities.DeliveryDetails;
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
		updateTicket(dto.getFoodPreparationTime());
		updateTimeToReachDestination(delivery, dto.getTimeToReachDestination());

		repo.saveAndFlush(delivery);
	}

	private void updateTicket(Integer foodPreparationTime) {
		if (foodPreparationTime != null) {
			// TODO update ticket
		}
	}

	private void updateTimeToReachDestination(DeliveryDetails delivery, LocalDateTime time) {
		if (time != null) {
			BeanValidator.validate(time, ValidAheadTime.class);
			delivery.setTimeToReachDestination(time);

			// TODO update ticket
		}
	}

	private void updateDeliveryStatus(DeliveryDetails delivery, DeliveryStatus status) {
		if (status != null) {
			BeanValidator.validate(status, ValidDeliveryStatus.class);
			delivery.setDeliveryStatus(status);
		}
	}

	private DeliveryDetails getDeliveryById(Long deliveryId) {

		return repo.findById(deliveryId).orElseThrow(() -> new NotFoundException("Delivery doesn't exist against Id: " + deliveryId));
	}
}
