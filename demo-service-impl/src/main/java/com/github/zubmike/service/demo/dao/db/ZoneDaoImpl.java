package com.github.zubmike.service.demo.dao.db;

import org.hibernate.SessionFactory;
import com.github.zubmike.service.dao.db.BasicEntityItemDao;
import com.github.zubmike.service.demo.dao.ZoneDao;
import com.github.zubmike.service.demo.types.Zone;

import javax.inject.Inject;

public class ZoneDaoImpl extends BasicEntityItemDao<Integer, Zone> implements ZoneDao {

	@Inject
	public ZoneDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, Zone.class);
	}

}
