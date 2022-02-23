package com.github.zubmike.service.demo.api.types;

import com.github.zubmike.core.types.BasicDictItem;

import java.io.Serial;

public class AuthUserInfo extends BasicDictItem {

	@Serial
	private static final long serialVersionUID = -3876150884246740857L;

	private String accessToken;

	public AuthUserInfo() {

	}

	public AuthUserInfo(int id, String name, String accessToken) {
		super(id, name);
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
