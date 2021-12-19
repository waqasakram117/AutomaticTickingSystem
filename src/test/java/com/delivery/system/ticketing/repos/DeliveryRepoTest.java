package com.delivery.system.ticketing.repos;

import static com.delivery.system.utils.UtcDateTimeUtils.utcTimeNow;
import static org.assertj.core.api.Assertions.assertThat;

import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=validate"})
@ActiveProfiles("test")
class DeliveryRepoTest {

	@Autowired
	private DeliveryRepo deliveryRepo;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(deliveryRepo).isNotNull();
	}

	@Test
	void shouldSuccessfullySaveDelivery() {
		var delivery = DeliveryMapper.map(prepareValidDeliveryDTO());
		var saved = deliveryRepo.save(delivery);

		assertThat(saved).isNotNull();
	}

	@Test
	void shouldGetNothingAfterLastModifiedTime() {
		var delivery = DeliveryMapper.map(prepareValidDeliveryDTO());
		delivery.setLastModified(utcTimeNow());
		var saved = deliveryRepo.save(delivery);

		var afterIntervalData = deliveryRepo.findAllWithLastModifiedAfter(utcTimeNow().plusSeconds(2));

		assertThat(afterIntervalData.size()).isZero();
	}

	@Test
	void shouldSuccessfullyGetDeliveriesFromSpecificLastModified() {
		var delivery = DeliveryMapper.map(prepareValidDeliveryDTO());
		delivery.setLastModified(utcTimeNow());
		var delivery2 = DeliveryMapper.map(prepareValidDeliveryDTO());
		delivery2.setLastModified(utcTimeNow());
		var saved = deliveryRepo.saveAll(List.of(delivery, delivery2));

		var afterIntervalData = deliveryRepo.findAllWithLastModifiedAfter(utcTimeNow().minusSeconds(2));

		assertThat(afterIntervalData.size()).isEqualTo(saved.size());
	}

	private NewDeliveryDto prepareValidDeliveryDTO() {

		var now = utcTimeNow();
		var dto = new NewDeliveryDto();
		dto.setDeliveryStatus("Order received");
		dto.setCustomerType("New");
		dto.setRiderRating(5);
		dto.setDestinationDistance(5);
		dto.setTimeToReachDestination(now.plusMinutes(30));
		dto.setExpectedDeliveryTime(now.plusMinutes(50));
		dto.setFoodPreparationTime(10);

		return dto;
	}
}