package com.delivery.system.config;

import com.delivery.system.security.pojos.internal.UserRequest;
import com.delivery.system.security.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile(value = {"!test"})
public class DemoUserInitializer {

	private final UserService userService;

	@Value("${ticket.test.user.name}")
	private String userName;

	@Value("${ticket.test.user.password}")
	private String password;

	public DemoUserInitializer(UserService userService) {
		this.userService = userService;
	}


	@EventListener(ApplicationReadyEvent.class)
	public void loadData() {
		userService.createUserIfNotExist(UserRequest.of(userName, password, "USER"));
	}
}