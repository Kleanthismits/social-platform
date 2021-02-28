package com.mitsioulis.socialplatform.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	AuthUserService authUserService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		// look Spring's BasicAuthenticationEntryPoint
		http.httpBasic().authenticationEntryPoint(new BasicAuthenticationEntryPoint());

		//// @formatter:off
		http
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/1.0/login").authenticated()
			.and()
			.authorizeRequests().anyRequest().permitAll();
		// @formatter:on

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authUserService).passwordEncoder(passwordEncoder());
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
