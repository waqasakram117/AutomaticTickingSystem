package com.delivery.system.security.pojos.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequest {

	@JsonProperty("username")
	private String userName;

	@JsonProperty("password")
	private String password;

	@JsonProperty("role")
	private String role;

	public UserRequest() {
		// for json marshaling
	}

	public static UserRequest of(String userName, String password, String role) {
		var u = new UserRequest();
		u.setPassword(password);
		u.setUserName(userName);
		u.setRole(role);
		return u;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
