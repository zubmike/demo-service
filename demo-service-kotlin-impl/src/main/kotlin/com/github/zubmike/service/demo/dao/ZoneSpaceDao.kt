package com.github.zubmike.service.demo.dao

import com.github.zubmike.core.dao.EntityItemDao
import com.github.zubmike.service.demo.types.ZoneSpace

interface ZoneSpaceDao : EntityItemDao<Long, ZoneSpace> {

    fun getAllByZone(zoneId: Int): List<ZoneSpace>

    fun getByStarship(starshipId: Long): ZoneSpace?

    fun getUsedSpace(zoneId: Int, starshipId: Long): ZoneSpace?

}
