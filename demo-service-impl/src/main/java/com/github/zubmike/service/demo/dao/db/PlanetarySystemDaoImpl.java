package com.github.zubmike.service.demo.dao.db;

import org.hibernate.SessionFactory;
import com.github.zubmike.service.dao.db.BasicDictItemDao;
import com.github.zubmike.service.demo.dao.PlanetarySystemDao;
import com.github.zubmike.service.demo.types.PlanetarySystem;

import javax.inject.Inject;
import java.util.Optional;

public class PlanetarySystemDaoImpl extends BasicDictItemDao<PlanetarySystem> implements PlanetarySystemDao {

	@Inject
	public PlanetarySystemDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, PlanetarySystem.class);
	}

	@Override
	public Optional<PlanetarySystem> getByCode(String code) {
		return runAndReturn(session -> session.createQuery("from PlanetarySystem where code = :code", PlanetarySystem.class)
				.setParameter("code", code)
				.uniqueResultOptional());
	}
}
