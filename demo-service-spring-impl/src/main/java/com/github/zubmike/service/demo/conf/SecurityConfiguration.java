package com.github.zubmike.service.demo.conf;

import com.github.zubmike.service.demo.types.ServiceUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class SecurityConfiguration {

	@Autowired
	private AuthFilter authFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf().disable()
				.authorizeRequests().antMatchers("/auth").permitAll()
				.anyRequest().authenticated();
		httpSecurity.addFilterAfter(authFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}

	@Bean
	@RequestScope
	public ServiceUserContext getServiceUserContext() {
		return (ServiceUserContext) SecurityContextHolder.getContext().getAuthentication();
	}

}
