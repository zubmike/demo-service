package ru.zubmike.service.demo.api;

import org.glassfish.jersey.server.ResourceConfig;

public class BasicResource {

	public BasicResource(ResourceConfig resourceConfig) {
		resourceConfig.register(this);
	}
}
