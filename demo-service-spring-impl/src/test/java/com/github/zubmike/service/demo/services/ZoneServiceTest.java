package com.github.zubmike.service.demo.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.service.demo.api.types.ZoneEntry;
import com.github.zubmike.service.demo.dao.StarshipRepository;
import com.github.zubmike.service.demo.dao.ZoneRepository;
import com.github.zubmike.service.demo.dao.ZoneSpaceRepository;
import com.github.zubmike.service.demo.types.Starship;
import com.github.zubmike.service.demo.types.Zone;
import com.github.zubmike.service.demo.types.ZoneSpace;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class ZoneServiceTest {

	private static final int TEST_ZONE_ID = 1;
	private static final int TEST_ZONE_MAX_SIZE = 4;

	private static final long TEST_STARSHIP_ID = 1L;
	private static final int DEFAULT_STARSHIP_TIME_COUNT = 1;

	@Mock
	private ZoneRepository zoneRepository;
	@Mock
	private ZoneSpaceRepository zoneSpaceRepository;
	@Mock
	private StarshipRepository starshipRepository;

	@InjectMocks
	private ZoneService zoneService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(zoneRepository.findById(Mockito.eq(TEST_ZONE_ID)))
				.thenAnswer(invocation -> {
					var item = new Zone();
					item.setMaxSize(TEST_ZONE_MAX_SIZE);
					item.setId(invocation.getArgument(0));
					return Optional.of(item);
				});
		Mockito.when(starshipRepository.findById(Mockito.eq(TEST_STARSHIP_ID)))
				.thenAnswer(invocation -> {
					var item = new Starship();
					item.setId(invocation.getArgument(0));
					item.setTimeCount(DEFAULT_STARSHIP_TIME_COUNT);
					return Optional.of(item);
				});
	}

	@Test
	public void addZone() {
		zoneService.addZone(new ZoneEntry("Test A", 4));
		Mockito.verify(zoneRepository).save(Mockito.any(Zone.class));
	}

	@Test(expected = InvalidParameterException.class)
	public void addZoneWithInvalidSize() {
		zoneService.addZone(new ZoneEntry("Test A", 1));
	}

	@Test
	public void addToZone() {
		Mockito.when(zoneSpaceRepository.findAllByZoneId(Mockito.eq(TEST_ZONE_ID))).thenReturn(Collections.emptyList());
		Mockito.doAnswer(invocation -> {
			Starship item = invocation.getArgument(0);
			Assert.assertTrue(item.getTimeCount() > DEFAULT_STARSHIP_TIME_COUNT);
			return null;
		}).when(starshipRepository).save(Mockito.any(Starship.class));
		zoneService.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
		Mockito.verify(zoneSpaceRepository).save(Mockito.any(ZoneSpace.class));
	}

	@Test(expected = InvalidParameterException.class)
	public void addDuplicateToZone() {
		Mockito.when(zoneSpaceRepository.findByStarshipId(Mockito.eq(TEST_STARSHIP_ID)))
				.thenReturn(Optional.of(new ZoneSpace()));
		zoneService.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}

	@Test(expected = InvalidParameterException.class)
	public void addToFullZone() {
		Mockito.when(zoneSpaceRepository.findAllByZoneId(Mockito.eq(TEST_ZONE_ID)))
				.thenReturn(Arrays.asList(new ZoneSpace(), new ZoneSpace(), new ZoneSpace(), new ZoneSpace()));
		zoneService.addToZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}

	@Test
	public void deleteFromZone() {
		Mockito.when(zoneSpaceRepository.getUsedSpace(Mockito.eq(TEST_ZONE_ID), Mockito.eq(TEST_STARSHIP_ID)))
				.thenReturn(Optional.of(new ZoneSpace()));
		zoneService.deleteFromZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}

	@Test(expected = InvalidParameterException.class)
	public void deleteFromEmptyZone() {
		Mockito.when(zoneSpaceRepository.getUsedSpace(Mockito.eq(TEST_ZONE_ID), Mockito.eq(TEST_STARSHIP_ID)))
				.thenReturn(Optional.empty());
		zoneService.deleteFromZone(TEST_ZONE_ID, TEST_STARSHIP_ID);
	}
}
