package com.delivery.system.security.pojos.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Token {

	@JsonProperty("Bearer")
	public final String bearer;

	private Token(String bearer) {
		this.bearer = bearer;
	}

	public static Token of(String bearer) {
		return new Token(bearer);
	}
}
