package com.delivery.system.ticketing.entities.convertors;

import com.delivery.system.ticketing.enums.TicketPriority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TicketPriorityConverter implements AttributeConverter<TicketPriority, Short> {

	@Override
	public Short convertToDatabaseColumn(TicketPriority parentType) {
		return parentType.value;
	}

	@Override
	public TicketPriority convertToEntityAttribute(Short parentValue) {
		return TicketPriority.getByValue(parentValue);
	}
}
