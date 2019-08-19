package com.github.zubmike.service.demo.logic;

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
import com.github.zubmike.service.demo.dao.PlanetarySystemDao;
import com.github.zubmike.service.demo.dao.StarshipDao;
import com.github.zubmike.service.demo.types.PlanetarySystem;
import com.github.zubmike.service.demo.types.Starship;

import java.time.LocalDateTime;
import java.util.Optional;

public class StarshipLogicTest {

	private static final PlanetarySystem TEST_PLANET_SYSTEM = new PlanetarySystem(1, "Test system", "TST");

	@Mock
	private StarshipDao starshipDao;

	@Mock
	private PlanetarySystemDao planetarySystemDao;

	@InjectMocks
	private StarshipLogic starshipLogic;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(planetarySystemDao.get(Mockito.eq(TEST_PLANET_SYSTEM.getId())))
				.thenReturn(Optional.of(TEST_PLANET_SYSTEM));
		Mockito.when(planetarySystemDao.getByCode(Mockito.eq(TEST_PLANET_SYSTEM.getCode())))
				.thenReturn(Optional.of(TEST_PLANET_SYSTEM));
	}

	@Test
	public void addStarship() {
		starshipLogic.addStarship(new StarshipEntry("TST-000001"));
	}

	@Test(expected = InvalidParameterException.class)
	public void addDuplicateStarship() {
		Mockito.when(starshipDao.getByNumber(Mockito.eq("TST-000002")))
				.thenAnswer(invocation -> {
					var item = new Starship();
					item.setId(0);
					item.setNumber(invocation.getArgument(0));
					return Optional.of(item);
				});
		starshipLogic.addStarship(new StarshipEntry("TST-000002"));
	}

	@Test(expected = InvalidParameterException.class)
	public void addStarshipWithInvalidNumber() {
		starshipLogic.addStarship(new StarshipEntry("123"));
	}

	@Test(expected = InvalidParameterException.class)
	public void addStarshipFromUnknownPlanet() {
		starshipLogic.addStarship(new StarshipEntry("QWE-000001"));
	}

	@Test
	public void getStarship() {
		Starship mockStarship = new Starship();
		mockStarship.setId(3L);
		mockStarship.setNumber("TST-000003");
		mockStarship.setPlanetarySystemId(TEST_PLANET_SYSTEM.getId());
		mockStarship.setTimeCount(99);
		mockStarship.setCreateDate(LocalDateTime.now());

		Mockito.when(starshipDao.get(Mockito.eq(3L)))
				.thenReturn(Optional.of(mockStarship));
		Mockito.when(planetarySystemDao.getName(Mockito.eq(TEST_PLANET_SYSTEM.getId())))
				.thenReturn(TEST_PLANET_SYSTEM.getName());

		StarshipInfo starshipInfo = starshipLogic.getStarship(3L);

		Assert.assertEquals(mockStarship.getId(), starshipInfo.getId());
		Assert.assertEquals(mockStarship.getNumber(), starshipInfo.getNumber());
		Assert.assertEquals(mockStarship.getPlanetarySystemId(), starshipInfo.getPlanetarySystemId());
		Assert.assertEquals(TEST_PLANET_SYSTEM.getName(), starshipInfo.getPlanetarySystemName());
		Assert.assertEquals(mockStarship.getTimeCount(), starshipInfo.getTimeCount());
		Assert.assertEquals(DateTimeUtils.toString(mockStarship.getCreateDate()), starshipInfo.getCreateDate());
	}

	@Test(expected = NotFoundException.class)
	public void getUnknownStarship() {
		starshipLogic.getStarship("TST-999999");
	}
}
