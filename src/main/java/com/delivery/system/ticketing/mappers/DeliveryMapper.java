package com.delivery.system.ticketing.mappers;

import com.delivery.system.ticketing.entities.DeliveryDetails;
import com.delivery.system.ticketing.enums.CustomerType;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;

public final class DeliveryMapper {
	private DeliveryMapper() {
	}

	public static DeliveryDetails map(NewDeliveryDto dto) {
		var delivery = new DeliveryDetails();
		delivery.setDeliveryStatus(DeliveryStatus.getByStatus(dto.getDeliveryStatus()));
		delivery.setCustomerType(CustomerType.getByType(dto.getCustomerType()));
		delivery.setDistanceFromDestination(dto.getDestinationDistance());
		delivery.setTimeToReachDestination(dto.getTimeToReachDestination());
		delivery.setExpectedDeliveryTime(dto.getExpectedDeliveryTime());

		return delivery;
	}

}
