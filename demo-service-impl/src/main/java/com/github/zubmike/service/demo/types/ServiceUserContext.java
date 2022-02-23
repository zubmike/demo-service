package com.github.zubmike.service.demo.types;

import javax.validation.constraints.NotNull;
import java.util.Locale;

public class ServiceUserContext {

	private int userId;
	private Locale locale;

	public ServiceUserContext() {
		this(-1, Locale.getDefault());
	}

	public ServiceUserContext(int userId, Locale locale) {
		this.userId = userId;
		this.locale = locale;
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
}
