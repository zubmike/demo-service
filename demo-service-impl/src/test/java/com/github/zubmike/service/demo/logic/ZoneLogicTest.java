package com.github.zubmike.service.demo.logic;

import com.github.zubmike.service.demo.types.ServiceUserContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.service.demo.api.types.ZoneEntry;
import com.github.zubmike.service.demo.dao.StarshipDao;
import com.github.zubmike.service.demo.dao.ZoneDao;
import com.github.zubmike.service.demo.dao.ZoneSpaceDao;
import com.github.zubmike.service.demo.types.Starship;
import com.github.zubmike.service.demo.types.Zone;
import com.github.zubmike.service.demo.types.ZoneSpace;
import com.github.zubmike.service.managers.TransactionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

public class ZoneLogicTest {

	private static final int TEST_ZONE_ID = 1;
	private static final int TEST_ZONE_MAX_SIZE = 4;

	private static final long TEST_STARSHIP_ID = 1L;
	private static final int DEFAULT_STARSHIP_TIME_COUNT = 1;

	@Spy
	private ServiceUserContext serviceUserContext = new ServiceUserContext(1, Locale.getDefault());
	@Mock
	private ZoneDao zoneDao;
	@Mock
	private ZoneSpaceDao zoneSpaceDao;
	@Mock
	private StarshipDao starshipDao;
	@Mock
	private TransactionManager transactionManager;

	@InjectMocks
	private ZoneLogic zoneLogic;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(zoneDao.get(Mockito.eq(TEST_ZONE_ID)))
				.thenAnswer(invocation -> {
					var item = new Zone();
					item.setMaxSize(TEST_ZONE_MAX_SIZE);
					item.setId(invocation.getArgument(0));
					return Optional.of(item);
				});
		Mockito.when(starshipDao.get(Mockito.eq(TEST_STARSHIP_ID)))
				.thenAnswer(invocation -> {
					var item = new Starship();
					item.setId(invocation.getArgument(0));
					item.setTimeCount(DEFAULT_STARSHIP_TIME_COUNT);
					return Optional.of(item);
				});
	}

	private void mockTransaction() {
		Mockito.doAnswer(invocation -> {
			((Runnable) invocation.getArgument(0)).run();
			return null;
		}).when(transactionManager).run(Mockito.any(Runnable.class));
	}

	@Test
	public void addZone() {
		zoneLogic.addZone(new ZoneEntry("Test A", 4));
		Mockito.verify(zoneDao).add(Mockito.any(Zone.class));
	}

	@Test(expected = InvalidParameterException.class)
	public void addZoneWithInvalidSize() {
		zoneLogic.addZone(new ZoneEntry("Test A", 1));
	}

	@Test
	public void addToZone() {
		Mockito.when(zoneSpaceDao.getAllByZone(Mockito.eq(TEST_ZONE_ID))).thenReturn(Collections.emptyList());
		mockTransaction();
		Mockito.doAnswer(invocation -> {
			Starship item = invocation.getArgument(0);
			Assert.assertTrue(item.getTimeCount() > DEFAULT_STARSHIP_TIME_COUNT);
			return null;
		}).when(starshipDao).update(Mockito.any(Starship.class));
		zoneLogic.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
		Mockito.verify(transactionManager).run(Mockito.any(Runnable.class));
		Mockito.verify(zoneSpaceDao).add(Mockito.any(ZoneSpace.class));
	}

	@Test(expected = InvalidParameterException.class)
	public void addDuplicateToZone() {
		Mockito.when(zoneSpaceDao.getByStarship(Mockito.eq(TEST_STARSHIP_ID)))
				.thenReturn(Optional.of(new ZoneSpace()));
		mockTransaction();
		zoneLogic.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}

	@Test(expected = InvalidParameterException.class)
	public void addToFullZone() {
		Mockito.when(zoneSpaceDao.getAllByZone(Mockito.eq(TEST_ZONE_ID)))
				.thenReturn(Arrays.asList(new ZoneSpace(), new ZoneSpace(), new ZoneSpace(), new ZoneSpace()));
		mockTransaction();
		zoneLogic.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}

	@Test
	public void deleteFromZone() {
		Mockito.when(zoneSpaceDao.getUsedSpace(Mockito.eq(TEST_ZONE_ID), Mockito.eq(TEST_STARSHIP_ID)))
				.thenReturn(Optional.of(new ZoneSpace()));
		zoneLogic.deleteFromZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}

	@Test(expected = InvalidParameterException.class)
	public void deleteFromEmptyZone() {
		Mockito.when(zoneSpaceDao.getUsedSpace(Mockito.eq(TEST_ZONE_ID), Mockito.eq(TEST_STARSHIP_ID)))
				.thenReturn(Optional.empty());
		zoneLogic.deleteFromZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}
}
