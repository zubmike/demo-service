package ru.zubmike.service.demo.dao

import ru.zubmike.core.dao.DictItemDao
import ru.zubmike.service.demo.types.PlanetarySystem

interface PlanetarySystemDao : DictItemDao<Int, PlanetarySystem> {

    fun getByCode(code: String): PlanetarySystem?

}
