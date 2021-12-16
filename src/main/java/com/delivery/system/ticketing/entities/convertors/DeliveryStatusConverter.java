package com.delivery.system.ticketing.entities.convertors;

import com.delivery.system.ticketing.enums.DeliveryStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, String> {

	@Override
	public String convertToDatabaseColumn(DeliveryStatus customerType) {
		return customerType.status;
	}

	@Override
	public DeliveryStatus convertToEntityAttribute(String type) {
		return DeliveryStatus.getByStatus(type);
	}
}
