package com.github.zubmike.service.demo.api.types;

import java.io.Serial;
import java.io.Serializable;

public class AuthEntry implements Serializable {

	@Serial
	private static final long serialVersionUID = -1073886954896245354L;

	private String login;
	private String password;

	public AuthEntry() {
	}

	public AuthEntry(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
