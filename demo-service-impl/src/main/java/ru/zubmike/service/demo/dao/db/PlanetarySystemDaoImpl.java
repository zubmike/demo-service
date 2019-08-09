package ru.zubmike.service.demo.dao.db;

import org.hibernate.SessionFactory;
import ru.zubmike.service.dao.db.BasicDictItemDao;
import ru.zubmike.service.demo.dao.PlanetarySystemDao;
import ru.zubmike.service.demo.types.PlanetarySystem;

import javax.inject.Inject;
import java.util.Optional;

public class PlanetarySystemDaoImpl extends BasicDictItemDao<PlanetarySystem> implements PlanetarySystemDao {

	@Inject
	public PlanetarySystemDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, PlanetarySystem.class);
	}

	@Override
	public Optional<PlanetarySystem> getByCode(String code) {
		return runAndReturn(session -> session.createQuery("from " + clazz.getSimpleName() + " where code = :code",
				PlanetarySystem.class)
				.setParameter("code", code)
				.uniqueResultOptional());
	}
}
