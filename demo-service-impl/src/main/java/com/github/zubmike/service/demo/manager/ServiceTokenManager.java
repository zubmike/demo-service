package com.github.zubmike.service.demo.manager;

import com.github.zubmike.service.conf.JwtTokenProperties;
import com.github.zubmike.service.demo.types.AccessToken;
import com.github.zubmike.service.managers.KeyStoreManager;
import com.github.zubmike.service.managers.SimpleJwtTokenManager;

import javax.inject.Inject;

public class ServiceTokenManager extends SimpleJwtTokenManager<AccessToken> {

	@Inject
	public ServiceTokenManager(KeyStoreManager keyStoreManager, JwtTokenProperties jwtTokenProperties) {
		super(keyStoreManager, jwtTokenProperties, AccessToken.class);
	}
}
