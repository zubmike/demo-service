package com.github.zubmike.service.demo.types;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class ServiceUserContext implements Authentication {

	private int userId;
	private Locale locale;
	private boolean authenticated;

	public ServiceUserContext(int userId, Locale locale) {
		this.userId = userId;
		this.locale = locale;
		this.authenticated = true;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@NotNull
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(@NotNull Locale locale) {
		this.locale = locale;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
	}

	@Override
	public String getName() {
		return null;
	}
}
