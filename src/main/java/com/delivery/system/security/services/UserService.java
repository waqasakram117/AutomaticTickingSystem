package com.delivery.system.security.services;


import com.delivery.system.exceptions.BadRequestException;
import com.delivery.system.security.entities.UserEntity;
import com.delivery.system.security.pojos.internal.UserRequest;
import com.delivery.system.security.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserEntity readUserByUsername(String username) {
		return userRepository.findByUserName(username).orElseThrow(EntityNotFoundException::new);
	}

	public void createUserIfNotExist(UserRequest userCreateRequest) {
		Optional<UserEntity> byUsername = userRepository.findByUserName(userCreateRequest.getUserName());
		if (byUsername.isEmpty()) {
			createNewUser(userCreateRequest);
		}
	}

	public void createUser(UserRequest userCreateRequest) {
		Optional<UserEntity> byUsername = userRepository.findByUserName(userCreateRequest.getUserName());

		if (byUsername.isPresent()) {
			throw new BadRequestException("User already registered. Please use different username.");
		}
		createNewUser(userCreateRequest);

	}

	private void createNewUser(UserRequest userCreateRequest) {
		var apiUser = new UserEntity();
		apiUser.setUserName(userCreateRequest.getUserName());
		apiUser.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
		apiUser.setRole(userCreateRequest.getRole());
		userRepository.save(apiUser);
	}
}
