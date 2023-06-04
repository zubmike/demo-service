package com.github.zubmike.service.demo.utils;

import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {

	public AuthException(String msg, Throwable t) {
		super(msg, t);
	}

	public AuthException(String msg) {
		super(msg);
	}

}
