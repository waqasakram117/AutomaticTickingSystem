package com.delivery.system.ticketing.entities.convertors;

import com.delivery.system.ticketing.enums.CustomerType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CustomerTypeConverter implements AttributeConverter<CustomerType, String> {

	@Override
	public String convertToDatabaseColumn(CustomerType customerType) {
		return customerType.type;
	}

	@Override
	public CustomerType convertToEntityAttribute(String type) {
		return CustomerType.getByType(type);
	}
}
