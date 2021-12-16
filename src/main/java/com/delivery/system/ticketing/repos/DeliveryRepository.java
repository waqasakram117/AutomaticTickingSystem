package com.delivery.system.ticketing.repos;

import com.delivery.system.ticketing.entities.DeliveryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryDetails, Long> {
}
