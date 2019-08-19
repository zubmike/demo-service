package com.github.zubmike.service.demo.dao.db

import org.hibernate.SessionFactory
import com.github.zubmike.service.dao.db.BasicEntityItemDao
import com.github.zubmike.service.demo.dao.StarshipDao
import com.github.zubmike.service.demo.types.Starship
import com.github.zubmike.service.demo.utils.uniqueResultNullable

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
