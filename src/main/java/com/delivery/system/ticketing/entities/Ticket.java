package com.delivery.system.ticketing.entities;

import com.delivery.system.ticketing.entities.convertors.TicketPriorityConverter;
import com.delivery.system.ticketing.enums.TicketPriority;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, columnDefinition = "INT UNSIGNED")
	private Long id;

	@Column(name = "delivery_db_id", columnDefinition = "INT UNSIGNED", nullable = false)
	private Long deliveryDbId;

	@Column(name = "priority", columnDefinition = "SMALLINT UNSIGNED")
	@Convert(converter = TicketPriorityConverter.class)
	private TicketPriority priority;

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