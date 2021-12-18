package com.delivery.system.ticketing.web;

import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.pojos.external.UpdateDeliveryDto;
import com.delivery.system.ticketing.pojos.internal.RegisteredDeliveryData;
import com.delivery.system.ticketing.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

	private final DeliveryService service;

	@Autowired
	public DeliveryController(DeliveryService service) {
		this.service = service;
	}

	@PostMapping()
	public RegisteredDeliveryData addNewDelivery(@RequestBody @Valid NewDeliveryDto dto) {
		return service.addNewDelivery(dto);
	}

	@PutMapping()
	public RegisteredDeliveryData updateDelivery(@RequestBody @Valid UpdateDeliveryDto dto) {
		return service.updateDelivery(dto);
	}

}
