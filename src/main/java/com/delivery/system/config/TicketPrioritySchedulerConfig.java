package com.delivery.system.config;

import com.delivery.system.ticketing.services.TicketPriorityScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class TicketPrioritySchedulerConfig {

	private final TicketPriorityScheduler scheduler;

	@Autowired
	public TicketPrioritySchedulerConfig(TicketPriorityScheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Scheduled(fixedDelayString = "${ticket.priority-interval}")
	public void scheduleFixedDelayTask() {
		scheduler.prioritiesTickets();
	}
}