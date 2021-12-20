package com.delivery.system.security.filters;

import static com.delivery.system.security.config.SecurityConstants.EXPIRATION_TIME;
import static com.delivery.system.security.config.SecurityConstants.HEADER_STRING;
import static com.delivery.system.security.config.SecurityConstants.SECRET;
import static com.delivery.system.security.config.SecurityConstants.TOKEN_PREFIX;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.delivery.system.security.pojos.internal.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
	                                            HttpServletResponse res) throws AuthenticationException {
		try {
			var creds = new ObjectMapper()
					.readValue(req.getInputStream(), UserRequest.class);

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUserName(),
							creds.getPassword(),
							new ArrayList<>())
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication auth) throws IOException {
		var userName = ( (User) auth.getPrincipal() ).getUsername();
		var token = JWT.create()
				.withSubject(userName)
				.withClaim("role", auth.getAuthorities().iterator().next().getAuthority())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SECRET.getBytes()));

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(
				"{\"" + HEADER_STRING + "\":\"" + TOKEN_PREFIX + token + "\"}"
		);

		response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

	}

}