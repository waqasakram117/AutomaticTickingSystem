package com.delivery.system;

import com.delivery.system.security.pojos.internal.UserRequest;
import com.delivery.system.security.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AutomaticTickingSystemApplication {

	@Value("${ticket.test.user.name}")
	private String userName;

	@Value("${ticket.test.user.password}")
	private String password;

	public static void main(String[] args) {
		SpringApplication.run(AutomaticTickingSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner setDefaultUser(UserService service) {
		return args -> {
			service.createUserIfNotExist(UserRequest.of(userName, password, "USER"));
		};
	}
}
