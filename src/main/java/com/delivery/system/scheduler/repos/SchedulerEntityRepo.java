package com.delivery.system.scheduler.repos;

import com.delivery.system.scheduler.entities.SchedulerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerEntityRepo extends JpaRepository<SchedulerEntity, Integer> {

//	SELECT column_name FROM table_name
//	ORDER BY column_name DESC
//	LIMIT 1;
//	@Query("select s from SchedulerEntity s order by s.lastSyncTime desc limit 1")
//	Optional<SchedulerEntity> getLastScheduler();

	Optional<SchedulerEntity> findFirstByOrderByLastSyncTimeDesc();

}
