package ru.zubmike.service.demo.conf;

import com.google.inject.AbstractModule;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import ru.zubmike.service.demo.api.ZoneResource;
import ru.zubmike.service.demo.dao.ZoneDao;
import ru.zubmike.service.demo.dao.db.ZoneDaoImpl;
import ru.zubmike.service.demo.logic.ZoneLogic;

import javax.inject.Singleton;

public class BindingModule extends AbstractModule {

	private final ResourceConfig resourceConfig;
	private final SessionFactory sessionFactory;

	public BindingModule(ResourceConfig resourceConfig, SessionFactory sessionFactory) {
		this.resourceConfig = resourceConfig;
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected void configure() {
		super.configure();

		bind(ResourceConfig.class).toInstance(resourceConfig);
		bind(SessionFactory.class).toInstance(sessionFactory);

		bind(ZoneDao.class).to(ZoneDaoImpl.class).in(Singleton.class);

		bind(ZoneLogic.class).in(Singleton.class);

		bind(ZoneResource.class).asEagerSingleton();

		bind(ExceptionMapperImpl.class).asEagerSingleton();
		bind(LoggingFilter.class).asEagerSingleton();
	}
}
