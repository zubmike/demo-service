package ru.zubmike.service.demo.logic

import ru.zubmike.core.utils.DateTimeUtils
import ru.zubmike.core.utils.InvalidParameterException
import ru.zubmike.core.utils.NotFoundException
import ru.zubmike.service.demo.api.types.ZoneEntry
import ru.zubmike.service.demo.api.types.ZoneInfo
import ru.zubmike.service.demo.api.types.ZoneStarshipInfo
import ru.zubmike.service.demo.dao.StarshipDao
import ru.zubmike.service.demo.dao.ZoneDao
import ru.zubmike.service.demo.dao.ZoneSpaceDao
import ru.zubmike.service.demo.types.Starship
import ru.zubmike.service.demo.types.Zone
import ru.zubmike.service.demo.types.ZoneSpace
import ru.zubmike.service.managers.TransactionManager
import java.time.LocalDateTime

class ZoneLogic(
        private val zoneDao: ZoneDao,
        private val zoneSpaceDao: ZoneSpaceDao,
        private val starshipDao: StarshipDao,
        private val transactionManager: TransactionManager
) {

    companion object {
        const val MIN_SIZE = 2
    }

    fun addZone(zoneEntry: ZoneEntry): ZoneInfo {
        checkZoneEntry(zoneEntry)
        val zone = Zone()
        zone.name = zoneEntry.name
        zone.maxSize = zoneEntry.maxSize
        zoneDao.add(zone)
        return createZoneInfo(zone)
    }

    fun getZones(): List<ZoneInfo> {
        return zoneDao.all
                .map { createZoneInfo(it) }
    }

    fun getZone(id: Int): ZoneInfo {
        return zoneDao.get(id)
                .map { createZoneInfo(it) }
                .orElseThrow { NotFoundException() }
    }

    fun addToZone(zoneId: Int, starshipId: Long) {
        transactionManager.run {
            val zone = zoneDao.get(zoneId).orElseThrow { NotFoundException() }
            val starship = starshipDao.get(starshipId).orElseThrow { NotFoundException() }
            checkFreeZoneSpace(zone, starship)
            val zoneSpace = createZoneSpace(zone, starship)
            zoneSpaceDao.add(zoneSpace)
            starship.timeCount = starship.timeCount + 1
            starshipDao.update(starship)
        }
    }

    private fun checkFreeZoneSpace(zone: Zone, starship: Starship) {
        if (zoneSpaceDao.getByStarship(starship.id) != null) {
            throw InvalidParameterException("Starship is already parked")
        }
        val usedSize = zoneSpaceDao.getAllByZone(zone.id).size
        if (usedSize >= zone.maxSize) {
            throw InvalidParameterException("Not found empty space")
        }
    }

    fun deleteFromZone(zoneId: Int, starshipId: Long) {
        val zone = zoneDao.get(zoneId).orElseThrow { NotFoundException() }
        val starship = starshipDao.get(starshipId).orElseThrow { NotFoundException() }
        val zoneSpace = getZoneSpace(zone, starship)
        zoneSpaceDao.remove(zoneSpace)
    }

    private fun getZoneSpace(zone: Zone, starship: Starship): ZoneSpace {
        return zoneSpaceDao.getUsedSpace(zone.id, starship.id) ?: throw InvalidParameterException("Starship is not parked in the zone")
    }

    fun getZoneStarships(zoneId: Int): List<ZoneStarshipInfo> {
        zoneDao.get(zoneId).orElseThrow { NotFoundException() }
        return zoneSpaceDao.getAllByZone(zoneId)
                .map { this.createParkedStarshipInfo(it) }
    }

    private fun createParkedStarshipInfo(item: ZoneSpace): ZoneStarshipInfo {
        val number = starshipDao.get(item.starshipId)
                .map { it.number }
                .orElse(null)
        return ZoneStarshipInfo(item.starshipId, number, DateTimeUtils.toString(item.createDate))
    }

    private fun checkZoneEntry(zoneEntry: ZoneEntry) {
        if (zoneEntry.name == null || zoneEntry.name.isEmpty()) {
            throw InvalidParameterException("Invalid name")
        }
        if (zoneEntry.maxSize < MIN_SIZE) {
            throw InvalidParameterException("Invalid size")
        }
    }

    private fun createZoneInfo(zone: Zone): ZoneInfo {
        return ZoneInfo(zone.id, zone.name, zone.maxSize)
    }

    private fun createZoneSpace(zone: Zone, starship: Starship): ZoneSpace {
        val zoneSpace = ZoneSpace()
        zoneSpace.zoneId = zone.id
        zoneSpace.starshipId = starship.id
        zoneSpace.createDate = LocalDateTime.now()
        return zoneSpace
    }

}
