package com.delivery.system.security.filters;

import static com.delivery.system.security.config.SecurityConstants.HEADER_STRING;
import static com.delivery.system.security.config.SecurityConstants.SECRET;
import static com.delivery.system.security.config.SecurityConstants.TOKEN_PREFIX;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
	                                HttpServletResponse res,
	                                FilterChain chain) throws IOException, ServletException {
		var header = req.getHeader(HEADER_STRING);

		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	// Reads the JWT from the Authorization header, and then uses JWT to validate the token
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		var token = request.getHeader(HEADER_STRING);

		if (token != null) {
			// parse the token
			var jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
					.build()
					.verify(token.replace(TOKEN_PREFIX, ""));

			var role = jwt.getClaim("role").asString();
			var user = jwt.getSubject();
			if (user != null) {
				// new arraylist means authorities
				return new UsernamePasswordAuthenticationToken(user, null, getAuthorities(role));
			}

			return null;
		}

		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return List.of(new SimpleGrantedAuthority(role));
	}
}