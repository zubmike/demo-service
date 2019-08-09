package ru.zubmike.service.demo.conf;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import ru.zubmike.service.conf.DataBaseProperties;
import ru.zubmike.service.demo.types.PlanetarySystem;
import ru.zubmike.service.demo.types.Starship;
import ru.zubmike.service.demo.types.Zone;

public class HibernateFactory {

	private static final Class<?>[] ENTITIES = new Class[] {
			Zone.class,
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

		setDefaultProperties(configuration);
		dataBaseProperties.getProperties().forEach(configuration::setProperty);

		for (Class<?> entity : entities) {
			configuration.addAnnotatedClass(entity);
		}

		return configuration;
	}

	private static void setDefaultProperties(Configuration configuration) {
		configuration.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "org.hibernate.context.internal.ThreadLocalSessionContext");
		configuration.setProperty(Environment.USE_QUERY_CACHE, "true");
		configuration.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "true");
		configuration.setProperty(Environment.CACHE_REGION_FACTORY, "jcache");
		configuration.setProperty("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
		configuration.setProperty("hibernate.javax.cache.uri", HibernateFactory.class.getResource("/ehcache.xml").toString());
		configuration.setProperty("hibernate.javax.cache.missing_cache_strategy", "create");
	}
}
