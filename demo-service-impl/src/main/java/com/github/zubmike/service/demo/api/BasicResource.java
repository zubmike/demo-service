package com.github.zubmike.service.demo.api;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BasicResource {

	public BasicResource(ResourceConfig resourceConfig) {
		resourceConfig.register(this);
	}
}
