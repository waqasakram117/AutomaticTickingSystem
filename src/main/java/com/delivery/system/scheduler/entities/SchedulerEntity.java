package com.delivery.system.scheduler.entities;


import com.delivery.system.utils.UtcDateTimeUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "SCHEDULER")
@Entity()
public class SchedulerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "last_sync_time", nullable = false)
	private LocalDateTime lastSyncTime;

	public static SchedulerEntity newInstance(){
		return new SchedulerEntity();
	}

	@PrePersist
	protected void onCreate() {
		var now = UtcDateTimeUtils.utcTimeNow();
		setLastSyncTime(now);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(LocalDateTime lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

}
