package com.github.zubmike.service.demo.conf;

import com.google.inject.Guice;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import com.github.zubmike.service.demo.utils.YamlUtils;

public class ServiceResourceConfig extends ResourceConfig {

	private static final String DEFAULT_CONFIGURATION_PATH = "config.yml";

	public ServiceResourceConfig() {
		ServiceProperties serviceProperties = YamlUtils.parse(DEFAULT_CONFIGURATION_PATH, ServiceProperties.class);
		SessionFactory sessionFactory = HibernateFactory.createSessionFactory(serviceProperties.getDataBase());
		Guice.createInjector(new BindingModule(this, sessionFactory));
	}

}
