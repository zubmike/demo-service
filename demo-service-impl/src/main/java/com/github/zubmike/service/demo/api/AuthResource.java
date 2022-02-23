package com.github.zubmike.service.demo.api;

import com.github.zubmike.service.demo.api.types.AuthEntry;
import com.github.zubmike.service.demo.api.types.AuthUserInfo;
import com.github.zubmike.service.demo.logic.AuthLogic;
import com.github.zubmike.service.demo.utils.LocaleUtils;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

	private final AuthLogic authLogic;

	@Inject
	public AuthResource(AuthLogic authLogic) {
		this.authLogic = authLogic;
	}

	@POST
	public AuthUserInfo authenticate(@Context HttpHeaders headers, AuthEntry authEntry) {
		return authLogic.authenticate(LocaleUtils.getLocale(headers), authEntry);
	}

}
