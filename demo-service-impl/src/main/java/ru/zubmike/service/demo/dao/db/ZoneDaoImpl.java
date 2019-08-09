package ru.zubmike.service.demo.dao.db;

import org.hibernate.SessionFactory;
import ru.zubmike.service.dao.db.BasicEntityItemDao;
import ru.zubmike.service.demo.dao.ZoneDao;
import ru.zubmike.service.demo.types.Zone;

import javax.inject.Inject;

public class ZoneDaoImpl extends BasicEntityItemDao<Integer, Zone> implements ZoneDao {

	@Inject
	public ZoneDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, Zone.class);
	}

}
