package com.delivery.system.security.services;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class AuthenticationUserDetailService implements UserDetailsService {

	private final UserService userService;

	public AuthenticationUserDetailService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var apiUser = userService.readUserByUsername(username);
		if (apiUser == null) {
			throw new UsernameNotFoundException(username);
		}
		return new org.springframework.security.core.userdetails.User(apiUser.getUserName(),
				apiUser.getPassword(), getAuthorities(apiUser.getRole()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}
}
