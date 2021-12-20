package com.delivery.system.ticketing.web;


import static com.delivery.system.ticketing.mappers.DeliveryMapper.mapToRegisteredData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.delivery.system.ticketing.entities.Delivery;
import com.delivery.system.ticketing.enums.DeliveryStatus;
import com.delivery.system.ticketing.mappers.DeliveryMapper;
import com.delivery.system.ticketing.pojos.external.NewDeliveryDto;
import com.delivery.system.ticketing.pojos.external.UpdateDeliveryDto;
import com.delivery.system.ticketing.pojos.internal.RegisteredDeliveryData;
import com.delivery.system.ticketing.services.DeliveryService;
import com.delivery.system.utils.UtcDateTimeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = DeliveryController.class)
class DeliveryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private DeliveryService deliveryService;

	@Test
	void whenValidUrlAndMethodAndContentTypeThenReturns200() throws Exception {

		var dto = prepareValidDeliveryDTO();

		mockMvc.perform(post("/delivery")
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	void whenValidValidationOfInvalidInputContentThenReturns400() throws Exception {

		var dto = prepareValidDeliveryDTO();
		dto.setCustomerType("po7*");

		mockMvc.perform(post("/delivery")
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	void whenValidInputThenMapsToBusinessModel() throws Exception {

		var dto = prepareValidDeliveryDTO();

		mockMvc.perform(post("/delivery")
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		var userCaptor = ArgumentCaptor.forClass(Delivery.class);
		var foodPreCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(deliveryService, times(1)).addNewDelivery(userCaptor.capture(), foodPreCaptor.capture());

		assertThat(userCaptor.getValue().getDeliveryStatus().status).isEqualTo(dto.getDeliveryStatus());
		assertThat(userCaptor.getValue().getCustomerType().type).isEqualTo(dto.getCustomerType());
		assertThat(userCaptor.getValue().getDestinationDistance()).isEqualTo(dto.getDestinationDistance());
		assertThat(userCaptor.getValue().getTimeToReachDestination()).isEqualTo(dto.getTimeToReachDestination());
		assertThat(userCaptor.getValue().getExpectedDeliveryTime()).isEqualTo(dto.getExpectedDeliveryTime());
		assertThat(foodPreCaptor.getValue()).isEqualTo(dto.getFoodPreparationTime());

	}

	@Test
	void whenValidContentDataThenSaveDelivery() throws Exception {

		var dto = prepareValidDeliveryDTO();

		given(deliveryService.addNewDelivery(any(Delivery.class), any(Integer.class)))
				.willAnswer(invocation -> mapToRegisteredData(invocation.getArgument(0)));

		MvcResult mvcResult = mockMvc.perform(post("/delivery")
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();


		RegisteredDeliveryData expectedResponseBody = mapToRegisteredData(DeliveryMapper.map(dto));

		String actualResponseBody = mvcResult.getResponse().getContentAsString().toLowerCase();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(
						objectMapper.writeValueAsString(expectedResponseBody).toLowerCase());
	}

	@Test
	void whenDeliveryIdMissingInUpdateContentThenReturns400() throws Exception {
		var dto = prepareValidUpdateDeliveryDTO();
		dto.setDeliveryId(null);

		mockMvc.perform(put("/delivery")
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}


	@Test
	void whenValidUpdateContentThenUpdateDelivery() throws Exception {

		var dto = prepareValidUpdateDeliveryDTO();
		var deliveryDto = prepareValidDeliveryDTO();
		var delivery = DeliveryMapper.map(deliveryDto);
		var updatedDelivery = mapToRegisteredData(prepareUpdatedCurrentDelivery(delivery, dto));

		given(deliveryService.updateDelivery(any(UpdateDeliveryDto.class)))
				.willAnswer(invocation ->
						mapToRegisteredData(prepareUpdatedCurrentDelivery(delivery, invocation.getArgument(0))));

		MvcResult mvcResult = mockMvc.perform(put("/delivery")
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String actualResponseBody = mvcResult.getResponse().getContentAsString().toLowerCase();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(
						objectMapper.writeValueAsString(updatedDelivery).toLowerCase());
	}


	private Delivery prepareUpdatedCurrentDelivery(Delivery delivery, UpdateDeliveryDto dto) {

		delivery.setId(dto.getDeliveryId());
		delivery.setDeliveryStatus(DeliveryStatus.getByStatus(dto.getDeliveryStatus()));
		delivery.setTimeToReachDestination(dto.getTimeToReachDestination());

		return delivery;
	}

	private UpdateDeliveryDto prepareValidUpdateDeliveryDTO() {
		var now = UtcDateTimeUtils.utcTimeNow();
		var dto = new UpdateDeliveryDto();
		dto.setDeliveryId(101L);
		dto.setDeliveryStatus("Order Preparing");
		dto.setTimeToReachDestination(now.plusMinutes(30));
		dto.setFoodPreparationTime(10);

		return dto;
	}

	private NewDeliveryDto prepareValidDeliveryDTO() {

		var now = UtcDateTimeUtils.utcTimeNow();
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