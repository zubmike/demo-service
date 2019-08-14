package ru.zubmike.service.demo.dao.db

import org.hibernate.SessionFactory
import ru.zubmike.service.dao.db.BasicDictItemDao
import ru.zubmike.service.demo.dao.PlanetarySystemDao
import ru.zubmike.service.demo.types.PlanetarySystem
import ru.zubmike.service.demo.utils.uniqueResultNullable

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
