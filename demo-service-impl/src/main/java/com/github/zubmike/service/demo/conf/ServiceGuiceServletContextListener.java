package com.github.zubmike.service.demo.conf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ServiceGuiceServletContextListener extends GuiceServletContextListener {

	private final ServiceProperties serviceProperties;

	public ServiceGuiceServletContextListener(ServiceProperties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	private static Injector create(ServiceProperties serviceProperties) {
		var sessionFactory = HibernateFactory.createSessionFactory(serviceProperties.getDataBase());
		return Guice.createInjector(new ServiceGuiceBindingModule(serviceProperties, sessionFactory));
	}

	@Override
	protected Injector getInjector() {
		return create(serviceProperties);
	}

}
