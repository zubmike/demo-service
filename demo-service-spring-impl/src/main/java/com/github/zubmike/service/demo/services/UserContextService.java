package com.github.zubmike.service.demo.services;

import com.github.zubmike.service.demo.types.ServiceUserContext;

public abstract class UserContextService {

	protected ServiceUserContext serviceUserContext;

	public UserContextService(ServiceUserContext serviceUserContext) {
		this.serviceUserContext = serviceUserContext;
	}

}
