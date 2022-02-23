package com.github.zubmike.service.demo.types;

import java.io.Serial;
import java.io.Serializable;

public class AccessToken implements Serializable {

	@Serial
	private static final long serialVersionUID = 2807250077158764649L;

	private int userId;

	public AccessToken() {
	}

	public AccessToken(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
