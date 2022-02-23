package com.github.zubmike.service.demo.conf;

import com.github.zubmike.service.utils.JsonUtils;
import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import javax.servlet.ServletContext;

public class ServiceResourceConfig extends ResourceConfig {

	@Inject
	public ServiceResourceConfig(ServletContext servletContext, ServiceLocator serviceLocator) {
		var injector = (Injector) servletContext.getAttribute(Injector.class.getName());
		init(serviceLocator, injector);
	}

	private void init(ServiceLocator serviceLocator, Injector injector) {
		registerGuice(serviceLocator, injector);
		packages("com.github.zubmike.service.demo.api");
		register(new JacksonJaxbJsonProvider(JsonUtils.createObjectMapper(), JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
	}

	private void registerGuice(ServiceLocator serviceLocator, Injector injector) {
		GuiceBridge.getGuiceBridge()
				.initializeGuiceBridge(serviceLocator);
		serviceLocator.getService(GuiceIntoHK2Bridge.class)
				.bridgeGuiceInjector(injector);
	}

}
