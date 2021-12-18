package com.delivery.system.ticketing.pojos.internal;

import java.time.LocalDateTime;

public interface RegisteredTicketData {

	Long getId();

	Long getDeliveryDbId();

	String getPriority();

	LocalDateTime getCreatedAt();

	LocalDateTime getLastModified();
}
