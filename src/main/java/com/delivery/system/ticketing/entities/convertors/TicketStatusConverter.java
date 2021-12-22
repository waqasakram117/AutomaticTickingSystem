package com.delivery.system.ticketing.entities.convertors;

import com.delivery.system.ticketing.enums.TicketStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TicketStatusConverter implements AttributeConverter<TicketStatus, Short> {

	@Override
	public Short convertToDatabaseColumn(TicketStatus attribute) {
		return attribute.status;
	}

	@Override
	public TicketStatus convertToEntityAttribute(Short value) {
		return TicketStatus.getByValue(value);
	}
}
