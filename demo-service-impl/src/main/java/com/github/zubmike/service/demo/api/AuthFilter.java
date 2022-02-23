package com.github.zubmike.service.demo.api;

import com.github.zubmike.service.demo.logic.AuthLogic;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.demo.utils.LocaleUtils;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Secure
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

	@Inject
	private AuthLogic authLogic;

	@Inject
	private javax.inject.Provider<ServiceUserContext> serviceUserContextProvider;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		var newServiceUserContext = authLogic.authorize(
				LocaleUtils.getLocale(requestContext),
				requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));
		var serviceUserContext = serviceUserContextProvider.get();
		serviceUserContext.setUserId(newServiceUserContext.getUserId());
		serviceUserContext.setLocale(newServiceUserContext.getLocale());
	}

}
