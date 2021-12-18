package com.delivery.system.config;

import com.delivery.system.ticketing.services.TicketPriorityScheduler;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class TicketPrioritySchedulerConfig {

	private final TicketPriorityScheduler scheduler;
	private LocalDateTime lastSchedulerTime = null;

	@Autowired
	public TicketPrioritySchedulerConfig(TicketPriorityScheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Scheduled(fixedDelayString = "${ticket.priority-interval}", timeUnit = TimeUnit.SECONDS)
	public void scheduleFixedDelayTask() {
		var now = UtcDateTimeUtils.utcTimeNow();
		scheduler.prioritiesTickets(lastSchedulerTime, now);
		lastSchedulerTime = now;
	}
}