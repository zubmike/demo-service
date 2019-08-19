package com.github.zubmike.service.demo.conf;

import com.google.inject.AbstractModule;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import com.github.zubmike.service.demo.api.DictionaryResource;
import com.github.zubmike.service.demo.api.StarshipResource;
import com.github.zubmike.service.demo.api.ZoneResource;
import com.github.zubmike.service.demo.dao.PlanetarySystemDao;
import com.github.zubmike.service.demo.dao.StarshipDao;
import com.github.zubmike.service.demo.dao.ZoneDao;
import com.github.zubmike.service.demo.dao.ZoneSpaceDao;
import com.github.zubmike.service.demo.dao.db.PlanetarySystemDaoImpl;
import com.github.zubmike.service.demo.dao.db.StarshipDaoImpl;
import com.github.zubmike.service.demo.dao.db.ZoneDaoImpl;
import com.github.zubmike.service.demo.dao.db.ZoneSpaceDaoImpl;
import com.github.zubmike.service.demo.logic.DictionaryLogic;
import com.github.zubmike.service.demo.logic.StarshipLogic;
import com.github.zubmike.service.demo.logic.ZoneLogic;
import com.github.zubmike.service.managers.DbTransactionManager;
import com.github.zubmike.service.managers.TransactionManager;

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
		bind(ZoneSpaceDao.class).to(ZoneSpaceDaoImpl.class).in(Singleton.class);
		bind(PlanetarySystemDao.class).to(PlanetarySystemDaoImpl.class).in(Singleton.class);
		bind(StarshipDao.class).to(StarshipDaoImpl.class).in(Singleton.class);

		bind(TransactionManager.class).toProvider(() -> new DbTransactionManager(sessionFactory)).in(Singleton.class);

		bind(ZoneLogic.class).in(Singleton.class);
		bind(StarshipLogic.class).in(Singleton.class);
		bind(DictionaryLogic.class).in(Singleton.class);

		bind(ZoneResource.class).asEagerSingleton();
		bind(StarshipResource.class).asEagerSingleton();
		bind(DictionaryResource.class).asEagerSingleton();

		bind(ExceptionMapperImpl.class).asEagerSingleton();
		bind(LoggingFilter.class).asEagerSingleton();
	}
}
