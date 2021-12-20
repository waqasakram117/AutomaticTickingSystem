package com.delivery.system.ticketing.web;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.delivery.system.ticketing.entities.Ticket;
import com.delivery.system.ticketing.enums.TicketPriority;
import com.delivery.system.ticketing.mappers.TicketMapper;
import com.delivery.system.ticketing.services.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = TicketController.class)
class TicketControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TicketService ticketService;


	@Test
	void whenValidUrlAndMethodAndContentTypeThenReturns200() throws Exception {
		mockMvc.perform(get("/tickets")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	void whenValidRequestAndNoDataToReturnThenReturnsEmptyListData() throws Exception {

		given(ticketService.getPriorityTickets())
				.willAnswer(invocation -> emptyList());

		var result = mockMvc.perform(get("/tickets")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertThat(result.getResponse().getContentAsString())
				.isEqualToIgnoringWhitespace(
						objectMapper.writeValueAsString(emptyList()).toLowerCase());

	}


	@Test
	void whenValidRequestAndDataExistsToReturnThenReturnsListData() throws Exception {

		var tickets = getTickets();
		given(ticketService.getPriorityTickets()).willAnswer(invocation -> tickets);

		var result = mockMvc.perform(get("/tickets")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertThat(result.getResponse().getContentAsString().toLowerCase())
				.isEqualToIgnoringWhitespace(
						objectMapper.writeValueAsString(tickets).toLowerCase());

	}

	private List<Ticket> getTickets() {
		return List.of(
				TicketMapper.map(1L, TicketPriority.HIGH),
				TicketMapper.map(2L, TicketPriority.HIGH),
				TicketMapper.map(3L, TicketPriority.LOW),
				TicketMapper.map(4L, TicketPriority.MEDIUM),
				TicketMapper.map(5L, TicketPriority.MEDIUM));
	}
}