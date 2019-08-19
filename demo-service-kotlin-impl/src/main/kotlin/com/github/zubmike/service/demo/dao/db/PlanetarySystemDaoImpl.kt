package com.github.zubmike.service.demo.dao.db

import org.hibernate.SessionFactory
import com.github.zubmike.service.dao.db.BasicDictItemDao
import com.github.zubmike.service.demo.dao.PlanetarySystemDao
import com.github.zubmike.service.demo.types.PlanetarySystem
import com.github.zubmike.service.demo.utils.uniqueResultNullable

class PlanetarySystemDaoImpl (sessionFactory: SessionFactory) : BasicDictItemDao<PlanetarySystem>(sessionFactory, PlanetarySystem::class.java), PlanetarySystemDao {

    override fun getByCode(code: String): PlanetarySystem? {
        return runAndReturn { session ->
            session.createQuery("from " + clazz.simpleName + " where code = :code",
                    PlanetarySystem::class.java)
                    .setParameter("code", code)
                    .uniqueResultNullable()
        }
    }
}
