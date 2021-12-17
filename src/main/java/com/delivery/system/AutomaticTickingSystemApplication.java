package com.delivery.system;

import com.delivery.system.ticketing.enums.CustomerType;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.services.DeliveryService;
import com.delivery.system.ticketing.services.TicketService;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutomaticTickingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomaticTickingSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner populateDeliveries(DeliveryService deliveryService, TicketService ticketService) {

		return args -> {
			for (int i = 1; i < 20; i++) {
				deliveryService.addNewDelivery(getRandomDelivery());
			}

			for (int i = 1; i < 20; i++) {
				ticketService.createTicket((long) i, TicketPriority.LOW);
			}

			for (int i = 10; i < 15; i++) {
				ticketService.updateTicketPriority((long) i, TicketPriority.HIGH);
			}

			for (int i = 15; i < 20; i++) {
				ticketService.updateTicketPriority((long) i, TicketPriority.MEDIUM);
			}

			ticketService.getPriorityTickets().forEach(ticket -> {
				System.out.printf("ID: %d, time: %d priority: %s %n", ticket.getId(),
						UtcDateTimeUtils.toUtcTimeStamp(ticket.getLastModified()),
						ticket.getPriority().name()
				);
			});
		};
	}


	private NewDeliveryDto getRandomDelivery() {
		var dto = new NewDeliveryDto();
		dto.setDeliveryStatus(DeliveryStatus.PREPARING.status);
		dto.setTimeToReachDestination(UtcDateTimeUtils.utcTimeNow().plusHours(1));
		dto.setExpectedDeliveryTime(UtcDateTimeUtils.utcTimeNow().plusHours(1));
		dto.setCustomerType(CustomerType.VIP.type);
		dto.setDestinationDistance(10000);
		dto.setRiderRating(4);

		return dto;
	}
}
