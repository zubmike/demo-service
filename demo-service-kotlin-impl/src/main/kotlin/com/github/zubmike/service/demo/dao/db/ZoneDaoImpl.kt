package com.github.zubmike.service.demo.dao.db

import org.hibernate.SessionFactory
import com.github.zubmike.service.dao.db.BasicEntityItemDao
import com.github.zubmike.service.demo.dao.ZoneDao
import com.github.zubmike.service.demo.types.Zone

class ZoneDaoImpl (sessionFactory: SessionFactory) : BasicEntityItemDao<Int, Zone>(sessionFactory, Zone::class.java), ZoneDao
