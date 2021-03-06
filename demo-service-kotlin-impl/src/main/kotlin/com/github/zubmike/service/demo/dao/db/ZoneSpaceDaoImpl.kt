package com.github.zubmike.service.demo.dao.db

import org.hibernate.SessionFactory
import com.github.zubmike.service.dao.db.BasicEntityItemDao
import com.github.zubmike.service.demo.dao.ZoneSpaceDao
import com.github.zubmike.service.demo.types.ZoneSpace
import com.github.zubmike.service.demo.utils.uniqueResultNullable

class ZoneSpaceDaoImpl (sessionFactory: SessionFactory) : BasicEntityItemDao<Long, ZoneSpace>(sessionFactory, ZoneSpace::class.java), ZoneSpaceDao {

    override fun getAllByZone(zoneId: Int): List<ZoneSpace> {
        return runAndReturn { session ->
            session.createQuery("from " + clazz.simpleName + " where zoneId = :zoneId", ZoneSpace::class.java)
                    .setParameter("zoneId", zoneId)
                    .list()
        }
    }

    override fun getByStarship(starshipId: Long): ZoneSpace? {
        return runAndReturn { session ->
            session.createQuery("from " + clazz.simpleName + " where starshipId = :starshipId", ZoneSpace::class.java)
                    .setParameter("starshipId", starshipId)
                    .uniqueResultNullable()
        }
    }

    override fun getUsedSpace(zoneId: Int, starshipId: Long): ZoneSpace? {
        return runAndReturn { session ->
            session.createQuery("from " + clazz.simpleName + " where zoneId = :zoneId and starshipId = :starshipId", ZoneSpace::class.java)
                    .setParameter("zoneId", zoneId)
                    .setParameter("starshipId", starshipId)
                    .uniqueResultNullable()
        }
    }
}
