package com.github.zubmike.service.demo.conf;

import com.github.zubmike.service.demo.types.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import com.github.zubmike.service.conf.DataBaseProperties;

public class HibernateFactory {

	private static final Class<?>[] ENTITIES = new Class[] {
			User.class,
			Zone.class,
			ZoneSpace.class,
			Starship.class,
			PlanetarySystem.class
	};

	public static SessionFactory createSessionFactory(DataBaseProperties dataBaseProperties) {
		Configuration hibernateConfiguration = createConfiguration(dataBaseProperties, ENTITIES);
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(hibernateConfiguration.getProperties());
		return hibernateConfiguration.buildSessionFactory(builder.build());
	}

	private static Configuration createConfiguration(DataBaseProperties dataBaseProperties, Class<?>... entities) {
		Configuration configuration = new Configuration();
		configuration.setProperty("hibernate.connection.driver_class", dataBaseProperties.getDriverClass());
		configuration.setProperty("hibernate.dialect", dataBaseProperties.getDialect());
		configuration.setProperty("hibernate.connection.url", dataBaseProperties.getUrl());
		if (dataBaseProperties.getUser() != null) {
			configuration.setProperty("hibernate.connection.username", dataBaseProperties.getUser());
		}
		if (dataBaseProperties.getPassword() != null) {
			configuration.setProperty("hibernate.connection.password", dataBaseProperties.getPassword());
		}
		dataBaseProperties.getProperties().forEach(configuration::setProperty);
		for (Class<?> entity : entities) {
			configuration.addAnnotatedClass(entity);
		}
		return configuration;
	}

}
