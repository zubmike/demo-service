package com.github.zubmike.service.demo.conf;

import com.github.zubmike.service.conf.FileKeyStoreProperties;
import com.github.zubmike.service.conf.JwtTokenProperties;
import com.github.zubmike.service.conf.ServerProperties;
import com.github.zubmike.service.demo.dao.*;
import com.github.zubmike.service.demo.dao.db.*;
import com.github.zubmike.service.demo.logic.AuthLogic;
import com.github.zubmike.service.demo.logic.DictionaryLogic;
import com.github.zubmike.service.demo.logic.StarshipLogic;
import com.github.zubmike.service.demo.logic.ZoneLogic;
import com.github.zubmike.service.demo.manager.ServiceKeyStoreManager;
import com.github.zubmike.service.demo.manager.ServiceTokenManager;
import com.github.zubmike.service.demo.manager.ServiceTransactionManager;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.managers.KeyStoreManager;
import com.github.zubmike.service.managers.TransactionManager;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import org.hibernate.SessionFactory;

import javax.inject.Singleton;

public class ServiceGuiceBindingModule extends ServletModule {

	private final ServiceProperties serviceProperties;
	private final SessionFactory sessionFactory;

	public ServiceGuiceBindingModule(ServiceProperties serviceProperties, SessionFactory sessionFactory) {
		this.serviceProperties = serviceProperties;
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected void configureServlets() {
		bind(ServiceProperties.class).toInstance(serviceProperties);
		bind(ServerProperties.class).toInstance(serviceProperties.getServer());
		bind(JwtTokenProperties.class).toInstance(serviceProperties.getJwtToken());
		bind(FileKeyStoreProperties.class).toInstance(serviceProperties.getKeyStore());

		bind(SessionFactory.class).toInstance(sessionFactory);

		bind(UserDao.class).to(UserDaoImpl.class).in(Singleton.class);
		bind(ZoneDao.class).to(ZoneDaoImpl.class).in(Singleton.class);
		bind(ZoneSpaceDao.class).to(ZoneSpaceDaoImpl.class).in(Singleton.class);
		bind(PlanetarySystemDao.class).to(PlanetarySystemDaoImpl.class).in(Singleton.class);
		bind(StarshipDao.class).to(StarshipDaoImpl.class).in(Singleton.class);

		bind(TransactionManager.class).to(ServiceTransactionManager.class).in(Singleton.class);
		bind(KeyStoreManager.class).to(ServiceKeyStoreManager.class).in(Singleton.class);
		bind(ServiceTokenManager.class).in(Singleton.class);

		bind(AuthLogic.class).in(Singleton.class);
		bind(DictionaryLogic.class).in(Singleton.class);
		bind(ZoneLogic.class).in(RequestScoped.class);
		bind(StarshipLogic.class).in(RequestScoped.class);

		bind(ServiceUserContext.class).in(RequestScoped.class);
	}
}
