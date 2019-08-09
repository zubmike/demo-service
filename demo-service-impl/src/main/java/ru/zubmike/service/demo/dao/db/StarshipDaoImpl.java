package ru.zubmike.service.demo.dao.db;

import org.hibernate.SessionFactory;
import ru.zubmike.service.dao.db.BasicEntityItemDao;
import ru.zubmike.service.demo.dao.StarshipDao;
import ru.zubmike.service.demo.types.Starship;

import javax.inject.Inject;
import java.util.Optional;

public class StarshipDaoImpl extends BasicEntityItemDao<Long, Starship> implements StarshipDao {

	@Inject
	public StarshipDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, Starship.class);
	}

	@Override
	public Optional<Starship> getByNumber(String number) {
		return runAndReturn(session -> session.createQuery("from " + clazz.getSimpleName() + " where number = :number",
				Starship.class)
				.setParameter("number", number)
				.uniqueResultOptional());
	}
}
