package com.github.zubmike.service.demo.dao

import com.github.zubmike.core.dao.DictItemDao
import com.github.zubmike.service.demo.types.PlanetarySystem

interface PlanetarySystemDao : DictItemDao<Int, PlanetarySystem> {

    fun getByCode(code: String): PlanetarySystem?

}
