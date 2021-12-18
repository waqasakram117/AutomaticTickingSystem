package com.delivery.system.ticketing.mappers;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.CustomerType;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.pojos.internal.RegisteredDeliveryData;

public final class DeliveryMapper {
	private DeliveryMapper() {
	}

	public static Delivery map(NewDeliveryDto dto) {
		var delivery = new Delivery();
		delivery.setDeliveryStatus(DeliveryStatus.getByStatus(dto.getDeliveryStatus()));
		delivery.setCustomerType(CustomerType.getByType(dto.getCustomerType()));
		delivery.setDestinationDistance(dto.getDestinationDistance());
		delivery.setTimeToReachDestination(dto.getTimeToReachDestination());
		delivery.setExpectedDeliveryTime(dto.getExpectedDeliveryTime());

		return delivery;
	}

	public static RegisteredDeliveryData mapToRegisteredData(Delivery delivery) {
		return RegisteredDeliveryData.builder()
				.id(delivery.getId())
				.deliveryStatus(delivery.getDeliveryStatus())
				.destinationDistance(delivery.getDestinationDistance())
				.expectedDeliveryTime(delivery.getExpectedDeliveryTime())
				.timeToReachDestination(delivery.getTimeToReachDestination())
				.createdAt(delivery.getCreatedAt())
				.customerType(delivery.getCustomerType())
				.lastModified(delivery.getLastModified())
				.build();
	}

}
