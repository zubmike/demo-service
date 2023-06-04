package com.github.zubmike.service.demo.services;

import com.github.zubmike.service.demo.types.ServiceUserContext;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class UserContextService {

	protected final ServiceUserContext serviceUserContext;

	public UserContextService() {
		this.serviceUserContext = (ServiceUserContext) SecurityContextHolder.getContext().getAuthentication();
	}

}
