package com.delivery.system.ticketing.entities;

import com.delivery.system.ticketing.entities.convertors.TicketPriorityConverter;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.utils.UtcDateTimeUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "TICKETS")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "INT UNSIGNED", nullable = false)
	private Long id;

	@Column(name = "delivery_db_id", columnDefinition = "INT UNSIGNED", nullable = false, updatable = false)
	private Long deliveryDbId;

	@Column(name = "priority", columnDefinition = "SMALLINT UNSIGNED")
	@Convert(converter = TicketPriorityConverter.class)
	private TicketPriority priority;

	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "last_modified")
	private LocalDateTime lastModified;

	@PrePersist
	protected void onCreate() {
		var now = UtcDateTimeUtils.utcTimeNow();
		setCreatedAt(now);
	}

	@PreUpdate
	protected void onUpdate() {
		var now = UtcDateTimeUtils.utcTimeNow();
		setLastModified(now);
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public Long getDeliveryDbId() {
		return deliveryDbId;
	}

	public void setDeliveryDbId(Long deliveryDbId) {
		this.deliveryDbId = deliveryDbId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TicketPriority getPriority() {
		return priority;
	}

	public void setPriority(TicketPriority priority) {
		this.priority = priority;
	}
}