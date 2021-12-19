package com.delivery.system.scheduler.config;

import com.delivery.system.scheduler.services.SchedulerEntityService;
import com.delivery.system.scheduler.services.TicketPriorityScheduler;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class TicketPrioritySchedulerConfig {

	private final TicketPriorityScheduler scheduler;
	private final SchedulerEntityService service;

	@Autowired
	public TicketPrioritySchedulerConfig(TicketPriorityScheduler scheduler, SchedulerEntityService service) {
		this.scheduler = scheduler;
		this.service = service;
	}

	@Scheduled(fixedDelayString = "${ticket.priority.interval.seconds}", timeUnit = TimeUnit.SECONDS)
	public void scheduleTicketPrioritizingTask() {
		var lastSyncTime = service.getLastSyncTime();
		scheduler.prioritiesTickets(lastSyncTime);
		service.updateLastSyncTime(UtcDateTimeUtils.utcTimeNow());
	}

}