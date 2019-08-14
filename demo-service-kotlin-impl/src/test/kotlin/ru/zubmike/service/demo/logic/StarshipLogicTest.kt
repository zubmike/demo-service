package ru.zubmike.service.demo.logic

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.zubmike.core.utils.DateTimeUtils
import ru.zubmike.core.utils.InvalidParameterException
import ru.zubmike.core.utils.NotFoundException
import ru.zubmike.service.demo.api.types.StarshipEntry
import ru.zubmike.service.demo.dao.PlanetarySystemDao
import ru.zubmike.service.demo.dao.StarshipDao
import ru.zubmike.service.demo.types.PlanetarySystem
import ru.zubmike.service.demo.types.Starship
import ru.zubmike.service.demo.utis.MockitoExt
import java.time.LocalDateTime
import java.util.*

class StarshipLogicTest {

    companion object {

        private val TEST_PLANET_SYSTEM = PlanetarySystem(1, "Test system", "TST")

    }

    @Mock
    private lateinit var starshipDao: StarshipDao

    @Mock
    private lateinit var planetarySystemDao: PlanetarySystemDao

    @InjectMocks
    private lateinit var starshipLogic: StarshipLogic

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        MockitoExt.whenever(planetarySystemDao.get(MockitoExt.safeEq<Int>(TEST_PLANET_SYSTEM.id)))
                .thenReturn(Optional.of(TEST_PLANET_SYSTEM))
        MockitoExt.whenever(planetarySystemDao.getByCode(MockitoExt.safeEq(TEST_PLANET_SYSTEM.code)))
                .thenReturn(TEST_PLANET_SYSTEM)
    }

    @Test
    fun addStarship() {
        starshipLogic.addStarship(StarshipEntry("TST-000001"))
    }

    @Test(expected = InvalidParameterException::class)
    fun addDuplicateStarship() {
        MockitoExt.whenever(starshipDao.getByNumber(MockitoExt.safeEq("TST-000002")))
                .thenAnswer { invocation ->
                    val item = Starship()
                    item.id = 0
                    item.number = invocation.getArgument<String>(0)
                    item
                }
        starshipLogic.addStarship(StarshipEntry("TST-000002"))
    }

    @Test(expected = InvalidParameterException::class)
    fun addStarshipWithInvalidNumber() {
        starshipLogic.addStarship(StarshipEntry("123"))
    }

    @Test(expected = InvalidParameterException::class)
    fun addStarshipFromUnknownPlanet() {
        starshipLogic.addStarship(StarshipEntry("QWE-000001"))
    }

    @Test
    fun getStarship() {
        val mockStarship = Starship()
        mockStarship.id = 3L
        mockStarship.number = "TST-000003"
        mockStarship.planetarySystemId = TEST_PLANET_SYSTEM.id
        mockStarship.timeCount = 99
        mockStarship.createDate = LocalDateTime.now()

        MockitoExt.whenever(starshipDao.get(MockitoExt.safeEq(3L)))
                .thenReturn(Optional.of(mockStarship))
        MockitoExt.whenever(planetarySystemDao.getName(MockitoExt.safeEq<Int>(TEST_PLANET_SYSTEM.id)))
                .thenReturn(TEST_PLANET_SYSTEM.name)

        val starshipInfo = starshipLogic.getStarship(3L)

        Assert.assertEquals(mockStarship.id, starshipInfo.id)
        Assert.assertEquals(mockStarship.number, starshipInfo.number)
        Assert.assertEquals(mockStarship.planetarySystemId.toLong(), starshipInfo.planetarySystemId.toLong())
        Assert.assertEquals(TEST_PLANET_SYSTEM.name, starshipInfo.planetarySystemName)
        Assert.assertEquals(mockStarship.timeCount.toLong(), starshipInfo.timeCount.toLong())
        Assert.assertEquals(DateTimeUtils.toString(mockStarship.createDate), starshipInfo.createDate)
    }

    @Test(expected = NotFoundException::class)
    fun getUnknownStarship() {
        starshipLogic.getStarship("TST-999999")
    }

}
