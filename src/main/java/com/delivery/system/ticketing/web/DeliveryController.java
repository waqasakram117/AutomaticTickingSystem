package com.delivery.system.ticketing.web;

import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.pojos.external.UpdateDeliveryDto;
import com.delivery.system.ticketing.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

	private final DeliveryService service;

	@Autowired
	public DeliveryController(DeliveryService service) {
		this.service = service;
	}

	@PostMapping()
	public void addNewDelivery(@Valid NewDeliveryDto dto) {
		service.addNewDelivery(dto);
	}

	@PutMapping()
	public void updateNewDelivery(UpdateDeliveryDto dto) {
		service.updateDelivery(dto);
	}

}
