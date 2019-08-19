package com.github.zubmike.service.demo.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.github.zubmike.core.utils.DateTimeUtils;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.demo.api.types.StarshipEntry;
import com.github.zubmike.service.demo.api.types.StarshipInfo;
import com.github.zubmike.service.demo.dao.PlanetarySystemRepository;
import com.github.zubmike.service.demo.dao.StarshipRepository;
import com.github.zubmike.service.demo.types.PlanetarySystem;
import com.github.zubmike.service.demo.types.Starship;

import java.time.LocalDateTime;
import java.util.Optional;

public class StarshipServiceTest {

	private static final PlanetarySystem TEST_PLANET_SYSTEM = new PlanetarySystem(1, "Test system", "TST");

	@Mock
	private StarshipRepository starshipRepository;

	@Mock
	private PlanetarySystemRepository planetarySystemRepository;

	@InjectMocks
	private StarshipService starshipService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(planetarySystemRepository.findById(Mockito.eq(TEST_PLANET_SYSTEM.getId())))
				.thenReturn(Optional.of(TEST_PLANET_SYSTEM));
		Mockito.when(planetarySystemRepository.findByCode(Mockito.eq(TEST_PLANET_SYSTEM.getCode())))
				.thenReturn(Optional.of(TEST_PLANET_SYSTEM));
	}

	@Test
	public void addStarship() {
		starshipService.addStarship(new StarshipEntry("TST-000001"));
	}

	@Test(expected = InvalidParameterException.class)
	public void addDuplicateStarship() {
		Mockito.when(starshipRepository.findByNumber(Mockito.eq("TST-000002")))
				.thenAnswer(invocation -> {
					var item = new Starship();
					item.setId(0);
					item.setNumber(invocation.getArgument(0));
					return Optional.of(item);
				});
		starshipService.addStarship(new StarshipEntry("TST-000002"));
	}

	@Test(expected = InvalidParameterException.class)
	public void addStarshipWithInvalidNumber() {
		starshipService.addStarship(new StarshipEntry("123"));
	}

	@Test(expected = InvalidParameterException.class)
	public void addStarshipFromUnknownPlanet() {
		starshipService.addStarship(new StarshipEntry("QWE-000001"));
	}

	@Test
	public void getStarship() {
		Starship mockStarship = new Starship();
		mockStarship.setId(3L);
		mockStarship.setNumber("TST-000003");
		mockStarship.setPlanetarySystemId(TEST_PLANET_SYSTEM.getId());
		mockStarship.setTimeCount(99);
		mockStarship.setCreateDate(LocalDateTime.now());

		Mockito.when(starshipRepository.findById(Mockito.eq(3L)))
				.thenReturn(Optional.of(mockStarship));

		StarshipInfo starshipInfo = starshipService.getStarship(3L);

		Assert.assertEquals(mockStarship.getId(), starshipInfo.getId());
		Assert.assertEquals(mockStarship.getNumber(), starshipInfo.getNumber());
		Assert.assertEquals(mockStarship.getPlanetarySystemId(), starshipInfo.getPlanetarySystemId());
		Assert.assertEquals(TEST_PLANET_SYSTEM.getName(), starshipInfo.getPlanetarySystemName());
		Assert.assertEquals(mockStarship.getTimeCount(), starshipInfo.getTimeCount());
		Assert.assertEquals(DateTimeUtils.toString(mockStarship.getCreateDate()), starshipInfo.getCreateDate());
	}

	@Test(expected = NotFoundException.class)
	public void getUnknownStarship() {
		starshipService.getStarship("TST-999999");
	}
}
