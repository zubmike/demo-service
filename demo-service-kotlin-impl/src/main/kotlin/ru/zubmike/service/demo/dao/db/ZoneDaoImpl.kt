package ru.zubmike.service.demo.dao.db

import org.hibernate.SessionFactory
import ru.zubmike.service.dao.db.BasicEntityItemDao
import ru.zubmike.service.demo.dao.ZoneDao
import ru.zubmike.service.demo.types.Zone

class ZoneDaoImpl (sessionFactory: SessionFactory) : BasicEntityItemDao<Int, Zone>(sessionFactory, Zone::class.java), ZoneDao
