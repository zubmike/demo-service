package ru.zubmike.service.demo.dao.db

import org.hibernate.SessionFactory
import ru.zubmike.service.dao.db.BasicEntityItemDao
import ru.zubmike.service.demo.dao.StarshipDao
import ru.zubmike.service.demo.types.Starship
import ru.zubmike.service.demo.utils.uniqueResultNullable

class StarshipDaoImpl (sessionFactory: SessionFactory) : BasicEntityItemDao<Long, Starship>(sessionFactory, Starship::class.java), StarshipDao {

    override fun getByNumber(number: String): Starship? {
        return runAndReturn { session ->
            session.createQuery("from " + clazz.simpleName + " where number = :number",
                    Starship::class.java)
                    .setParameter("number", number)
                    .uniqueResultNullable()
        }
    }
}
