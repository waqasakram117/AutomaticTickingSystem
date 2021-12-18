package com.delivery.system.ticketing.web;

import com.delivery.system.ticketing.pojos.internal.RegisteredTicketData;
import com.delivery.system.ticketing.services.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

	private final TicketService ticketService;

	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GetMapping()
	public List<RegisteredTicketData> getTickets() {
		return ticketService.getPriorityTickets();
	}
}
