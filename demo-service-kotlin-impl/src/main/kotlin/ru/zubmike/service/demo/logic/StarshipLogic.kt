package ru.zubmike.service.demo.logic

import ru.zubmike.core.utils.DateTimeUtils
import ru.zubmike.core.utils.InvalidParameterException
import ru.zubmike.core.utils.NotFoundException
import ru.zubmike.service.demo.api.types.StarshipEntry
import ru.zubmike.service.demo.api.types.StarshipInfo
import ru.zubmike.service.demo.dao.PlanetarySystemDao
import ru.zubmike.service.demo.dao.StarshipDao
import ru.zubmike.service.demo.types.PlanetarySystem
import ru.zubmike.service.demo.types.Starship

import java.time.LocalDateTime
import java.util.regex.Pattern

class StarshipLogic (private val starshipDao: StarshipDao, private val planetarySystemDao: PlanetarySystemDao) {

    companion object {
        private val NUMBER_PATTERN = Pattern.compile("^([A-Z]{3})-([0-9]{6})$")
    }

    fun addStarship(starshipEntry: StarshipEntry): StarshipInfo {
        val planetarySystem = parsePlanetarySystem(starshipEntry)
        if (starshipDao.getByNumber(starshipEntry.number) != null) {
            throw InvalidParameterException("Duplicate starship")
        }
        val starship = Starship()
        starship.number = starshipEntry.number
        starship.planetarySystemId = planetarySystem.id
        starship.createDate = LocalDateTime.now()
        starship.timeCount = 0
        starshipDao.add(starship)
        return createStarshipInfo(starship)
    }

    private fun parsePlanetarySystem(starshipEntry: StarshipEntry): PlanetarySystem {
        val matcher = NUMBER_PATTERN.matcher(starshipEntry.number)
        if (!matcher.find() || matcher.groupCount() < 2) {
            throw InvalidParameterException("Invalid number format")
        }
        return planetarySystemDao.getByCode(matcher.group(1)) ?: throw InvalidParameterException("Unknown planetary system")
    }

    private fun createStarshipInfo(starship: Starship): StarshipInfo {
        return StarshipInfo(
                starship.id,
                starship.number,
                starship.planetarySystemId,
                planetarySystemDao.getName(starship.planetarySystemId),
                DateTimeUtils.toString(starship.createDate),
                starship.timeCount
        )
    }

    fun getStarship(id: Long): StarshipInfo {
        return starshipDao.get(id)
                .map { this.createStarshipInfo(it) }
                .orElseThrow { NotFoundException() }
    }

    fun getStarship(number: String): StarshipInfo {
        return createStarshipInfo(starshipDao.getByNumber(number) ?: throw NotFoundException())
    }

}
