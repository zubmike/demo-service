package com.github.zubmike.service.demo.dao.db;

import org.hibernate.SessionFactory;
import com.github.zubmike.service.dao.db.BasicEntityItemDao;
import com.github.zubmike.service.demo.dao.ZoneSpaceDao;
import com.github.zubmike.service.demo.types.ZoneSpace;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class ZoneSpaceDaoImpl extends BasicEntityItemDao<Long, ZoneSpace> implements ZoneSpaceDao {

	@Inject
	public ZoneSpaceDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ZoneSpace.class);
	}

	@Override
	public List<ZoneSpace> getAllByZone(int zoneId) {
		return runAndReturn(session -> session
				.createQuery("from " + clazz.getSimpleName() + " where zoneId = :zoneId", ZoneSpace.class)
				.setParameter("zoneId", zoneId)
				.list());
	}

	@Override
	public Optional<ZoneSpace> getByStarship(long starshipId) {
		return runAndReturn(session -> session
				.createQuery("from " + clazz.getSimpleName() + " where starshipId = :starshipId", ZoneSpace.class)
				.setParameter("starshipId", starshipId)
				.uniqueResultOptional());
	}

	@Override
	public Optional<ZoneSpace> getUsedSpace(int zoneId, long starshipId) {
		return runAndReturn(session -> session
				.createQuery("from " + clazz.getSimpleName() + " where zoneId = :zoneId and starshipId = :starshipId", ZoneSpace.class)
				.setParameter("zoneId", zoneId)
				.setParameter("starshipId", starshipId)
				.uniqueResultOptional());
	}
}
