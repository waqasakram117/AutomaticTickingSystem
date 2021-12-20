package com.delivery.system.scheduler.repos;

import com.delivery.system.scheduler.entities.SchedulerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerEntityRepo extends JpaRepository<SchedulerEntity, Integer> {

	Optional<SchedulerEntity> findFirstByOrderByLastSyncTimeDesc();

}
