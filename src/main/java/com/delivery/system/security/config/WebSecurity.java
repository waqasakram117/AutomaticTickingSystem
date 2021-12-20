package com.delivery.system.security.config;

import static com.delivery.system.security.config.SecurityConstants.SIGN_UP_URL;

import com.delivery.system.security.filters.JWTAuthenticationFilter;
import com.delivery.system.security.filters.JWTAuthorizationFilter;
import com.delivery.system.security.services.AuthenticationUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final AuthenticationUserDetailService authUserService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(AuthenticationUserDetailService authUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.authUserService = authUserService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authUserService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
				.antMatchers("/tickets/**").hasAnyAuthority("USER")
				.antMatchers("/deliveries/**").hasAnyAuthority("USER")
				.anyRequest().authenticated()
				.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
				// this disables session creation on Spring Security
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

}