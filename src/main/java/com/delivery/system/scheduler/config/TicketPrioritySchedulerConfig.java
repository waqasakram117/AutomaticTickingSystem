package com.delivery.system.scheduler.config;

import com.delivery.system.scheduler.services.SchedulerEntityService;
import com.delivery.system.scheduler.services.TicketPriorityScheduler;
import com.delivery.system.utils.UtcDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class TicketPrioritySchedulerConfig {

	private static final Logger log = LoggerFactory.getLogger(TicketPrioritySchedulerConfig.class);
	private final TicketPriorityScheduler priorityScheduler;
	private final SchedulerEntityService scheduler;

	public TicketPrioritySchedulerConfig(TicketPriorityScheduler priorityScheduler, SchedulerEntityService scheduler) {
		this.priorityScheduler = priorityScheduler;
		this.scheduler = scheduler;
	}

	@Scheduled(fixedDelayString = "${ticket.priority.interval.seconds}", timeUnit = TimeUnit.SECONDS)
	public void scheduleTicketPrioritizingTask() {
		var lastSyncTime = scheduler.getLastSyncTime();
		log.info("Last sync time {}", lastSyncTime);
		priorityScheduler.prioritiesTickets();
		scheduler.updateLastSyncTime(UtcDateTimeUtils.utcTimeNow());
	}

}