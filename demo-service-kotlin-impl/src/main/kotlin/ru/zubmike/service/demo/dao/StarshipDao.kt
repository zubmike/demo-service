package ru.zubmike.service.demo.dao

import ru.zubmike.core.dao.EntityItemDao
import ru.zubmike.service.demo.types.Starship

interface StarshipDao : EntityItemDao<Long, Starship> {

    fun getByNumber(number: String): Starship?

}
