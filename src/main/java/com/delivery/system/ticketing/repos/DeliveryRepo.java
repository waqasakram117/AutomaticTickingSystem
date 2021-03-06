package com.delivery.system.ticketing.repos;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, Long> {

	@Query("select delivery from Delivery delivery" +
			" where delivery.deliveryStatus <> :deliveryStatus " +
			" and delivery.expectedDeliveryTime < :currentTime")
	List<Delivery> findAllDeliveriesByNotStatusAndCurrentTimeIsMoreThanExpected(
			@Param("deliveryStatus") DeliveryStatus notStatus,
			@Param("currentTime") LocalDateTime currentTime);
}
