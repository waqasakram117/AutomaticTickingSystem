package com.delivery.system.ticketing.repos;

import com.delivery.system.ticketing.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, Long> {

	@Query("select delivery from Delivery delivery" +
			" where delivery.lastModified >=:from")
	List<Delivery> findAllWithLastModifiedAfter(@Param("from") LocalDateTime from);
}
