package com.github.zubmike.service.demo.dao

import com.github.zubmike.core.dao.EntityItemDao
import com.github.zubmike.service.demo.types.Starship

interface StarshipDao : EntityItemDao<Long, Starship> {

    fun getByNumber(number: String): Starship?

}
