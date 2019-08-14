package ru.zubmike.service.demo.dao

import ru.zubmike.core.dao.EntityItemDao
import ru.zubmike.service.demo.types.ZoneSpace

interface ZoneSpaceDao : EntityItemDao<Long, ZoneSpace> {

    fun getAllByZone(zoneId: Int): List<ZoneSpace>

    fun getByStarship(starshipId: Long): ZoneSpace?

    fun getUsedSpace(zoneId: Int, starshipId: Long): ZoneSpace?

}
