package com.delivery.system.ticketing.web;

import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.pojos.external.UpdateDeliveryDto;
import com.delivery.system.ticketing.pojos.internal.RegisteredDeliveryData;
import com.delivery.system.ticketing.services.DeliveryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

	private final DeliveryService service;

	public DeliveryController(DeliveryService service) {
		this.service = service;
	}

	@PostMapping()
	public RegisteredDeliveryData addNewDelivery(@RequestBody @Valid NewDeliveryDto dto) {
		return service.addNewDelivery(DeliveryMapper.map(dto), dto.getFoodPreparationTime());
	}

	@PutMapping()
	public RegisteredDeliveryData updateDelivery(@RequestBody @Valid UpdateDeliveryDto dto) {
		return service.updateDelivery(dto);
	}

}
