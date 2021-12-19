package com.delivery.system.scheduler.services;

import com.delivery.system.scheduler.entities.SchedulerEntity;
import com.delivery.system.scheduler.repos.SchedulerEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SchedulerEntityService {

	private final SchedulerEntityRepo repo;

	@Autowired
	public SchedulerEntityService(SchedulerEntityRepo repo) {
		this.repo = repo;
	}

	public LocalDateTime getLastSyncTime(){
		var lastScheduler = repo.findFirstByOrderByLastSyncTimeDesc();
		if (lastScheduler.isPresent())
			return lastScheduler.get().getLastSyncTime();

		var newlyCreatedScheduler =  repo.saveAndFlush(SchedulerEntity.newInstance());

		return newlyCreatedScheduler.getLastSyncTime();
	}

	public void updateLastSyncTime(LocalDateTime syncTime){
		var scheduler = repo.findFirstByOrderByLastSyncTimeDesc().orElseThrow(() -> new RuntimeException("No scheduler found"));
		scheduler.setLastSyncTime(syncTime);
		repo.saveAndFlush(scheduler);
	}
}
