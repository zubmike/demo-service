package com.github.zubmike.service.demo.logic

import com.github.zubmike.core.utils.InvalidParameterException
import com.github.zubmike.service.demo.api.types.ZoneEntry
import com.github.zubmike.service.demo.dao.StarshipDao
import com.github.zubmike.service.demo.dao.ZoneDao
import com.github.zubmike.service.demo.dao.ZoneSpaceDao
import com.github.zubmike.service.demo.types.Starship
import com.github.zubmike.service.demo.types.Zone
import com.github.zubmike.service.demo.types.ZoneSpace
import com.github.zubmike.service.demo.utils.MockitoExt
import com.github.zubmike.service.managers.TransactionManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class ZoneLogicTest {

    companion object {

        private const val TEST_ZONE_ID = 1
        private const val TEST_ZONE_MAX_SIZE = 4

        private const val TEST_STARSHIP_ID = 1L
        private const val DEFAULT_STARSHIP_TIME_COUNT = 1
    }

    @Mock
    private lateinit var zoneDao: ZoneDao
    @Mock
    private lateinit var zoneSpaceDao: ZoneSpaceDao
    @Mock
    private lateinit var starshipDao: StarshipDao
    @Mock
    private lateinit var transactionManager: TransactionManager

    @InjectMocks
    private val zoneLogic: ZoneLogic? = null

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        MockitoExt.whenever(zoneDao.get(MockitoExt.safeEq(TEST_ZONE_ID)))
                .thenAnswer { invocation ->
                    val item = Zone()
                    item.maxSize = TEST_ZONE_MAX_SIZE
                    item.id = invocation.getArgument(0)
                    Optional.of(item)
                }
        MockitoExt.whenever(starshipDao.get(MockitoExt.safeEq(TEST_STARSHIP_ID)))
                .thenAnswer { invocation ->
                    val item = Starship()
                    item.id = invocation.getArgument(0)
                    item.timeCount = DEFAULT_STARSHIP_TIME_COUNT
                    Optional.of(item)
                }
    }

    private fun mockTransaction() {
        Mockito.doAnswer { invocation ->
            (invocation.getArgument<Any>(0) as Runnable).run()
            null
        }.`when`(transactionManager).run(Mockito.any(Runnable::class.java))
    }

    @Test
    fun addZone() {
        zoneLogic!!.addZone(ZoneEntry("Test A", 4))
        Mockito.verify(zoneDao).add(Mockito.any(Zone::class.java))
    }

    @Test(expected = InvalidParameterException::class)
    fun addZoneWithInvalidSize() {
        zoneLogic!!.addZone(ZoneEntry("Test A", 1))
    }

    @Test
    fun addToZone() {
        MockitoExt.whenever(zoneSpaceDao.getAllByZone(MockitoExt.safeEq(TEST_ZONE_ID))).thenReturn(emptyList())
        mockTransaction()
        Mockito.doAnswer { invocation ->
            val item = invocation.getArgument<Starship>(0)
            Assert.assertTrue(item.timeCount > DEFAULT_STARSHIP_TIME_COUNT)
            null
        }.`when`(starshipDao).update(Mockito.any(Starship::class.java))
        zoneLogic!!.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID)
        Mockito.verify(transactionManager).run(Mockito.any(Runnable::class.java))
        Mockito.verify(zoneSpaceDao).add(Mockito.any(ZoneSpace::class.java))
    }

    @Test(expected = InvalidParameterException::class)
    fun addDuplicateToZone() {
        MockitoExt.whenever(zoneSpaceDao.getByStarship(MockitoExt.safeEq(TEST_STARSHIP_ID)))
                .thenReturn(ZoneSpace())
        mockTransaction()
        zoneLogic!!.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID)
    }

    @Test(expected = InvalidParameterException::class)
    fun addToFullZone() {
        MockitoExt.whenever(zoneSpaceDao.getAllByZone(MockitoExt.safeEq(TEST_ZONE_ID)))
                .thenReturn(listOf(ZoneSpace(), ZoneSpace(), ZoneSpace(), ZoneSpace()))
        mockTransaction()
        zoneLogic!!.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID)
    }

    @Test
    fun deleteFromZone() {
        MockitoExt.whenever(zoneSpaceDao.getUsedSpace(MockitoExt.safeEq(TEST_ZONE_ID), MockitoExt.safeEq(TEST_STARSHIP_ID)))
                .thenReturn(ZoneSpace())
        zoneLogic!!.deleteFromZone(TEST_ZONE_ID, TEST_STARSHIP_ID)
    }

    @Test(expected = InvalidParameterException::class)
    fun deleteFromEmptyZone() {
        MockitoExt.whenever(zoneSpaceDao.getUsedSpace(MockitoExt.safeEq(TEST_ZONE_ID), MockitoExt.safeEq(TEST_STARSHIP_ID)))
                .thenReturn(null)
        zoneLogic!!.deleteFromZone(TEST_ZONE_ID, TEST_STARSHIP_ID)
    }


}
