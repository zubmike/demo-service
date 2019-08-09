package ru.zubmike.service.demo.conf;

import com.google.inject.Guice;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import ru.zubmike.service.utils.JsonUtils;

import java.io.File;
import java.io.IOException;

public class ServiceResourceConfig extends ResourceConfig {

	private static final String DEFAULT_CONFIGURATION_PATH = "config.json";

	public ServiceResourceConfig() throws IOException {
		ServiceProperties serviceProperties = JsonUtils.createObjectMapper().readValue(new File(DEFAULT_CONFIGURATION_PATH), ServiceProperties.class);
		SessionFactory sessionFactory = HibernateFactory.createSessionFactory(serviceProperties.getDataBase());
		Guice.createInjector(new BindingModule(this, sessionFactory));
	}

}
