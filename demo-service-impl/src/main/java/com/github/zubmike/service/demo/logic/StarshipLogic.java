package com.github.zubmike.service.demo.logic;

import com.github.zubmike.core.utils.DateTimeUtils;
import com.github.zubmike.core.utils.DuplicateException;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.api.types.StarshipEntry;
import com.github.zubmike.service.demo.api.types.StarshipInfo;
import com.github.zubmike.service.demo.dao.PlanetarySystemDao;
import com.github.zubmike.service.demo.dao.StarshipDao;
import com.github.zubmike.service.demo.types.PlanetarySystem;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.demo.types.Starship;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class StarshipLogic {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^([A-Z]{3})-([0-9]{6})$");

	private final ServiceUserContext serviceUserContext;

	private final StarshipDao starshipDao;
	private final PlanetarySystemDao planetarySystemDao;

	@Inject
	public StarshipLogic(ServiceUserContext serviceUserContext, StarshipDao starshipDao, PlanetarySystemDao planetarySystemDao) {
		this.serviceUserContext = serviceUserContext;
		this.starshipDao = starshipDao;
		this.planetarySystemDao = planetarySystemDao;
	}

	public StarshipInfo addStarship(StarshipEntry starshipEntry) {
		var planetarySystem = parsePlanetarySystem(starshipEntry);
		starshipDao.getByNumber(starshipEntry.getNumber()).ifPresent(it -> {
			throw new DuplicateException(ServiceResource.getString(serviceUserContext, "res.string.duplicate"));
		});
		var starship = new Starship();
		starship.setNumber(starshipEntry.getNumber());
		starship.setPlanetarySystemId(planetarySystem.getId());
		starship.setCreateDate(LocalDateTime.now());
		starship.setTimeCount(0);
		starshipDao.add(starship);
		return createStarshipInfo(starship);
	}

	private PlanetarySystem parsePlanetarySystem(StarshipEntry starshipEntry) {
		var matcher =  NUMBER_PATTERN.matcher(starshipEntry.getNumber());
		if (!matcher.find() || matcher.groupCount() < 2) {
			throw new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.invalidNumberFormat"));
		}
		return planetarySystemDao.getByCode(matcher.group(1))
				.orElseThrow(() -> new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.unknownPlanetarySystem")));
	}

	private StarshipInfo createStarshipInfo(Starship starship) {
		return new StarshipInfo(
				starship.getId(),
				starship.getNumber(),
				starship.getPlanetarySystemId(),
				planetarySystemDao.getName(starship.getPlanetarySystemId()),
				DateTimeUtils.toString(starship.getCreateDate()),
				starship.getTimeCount()
		);
	}

	public StarshipInfo getStarship(long id) {
		return starshipDao.get(id)
				.map(this::createStarshipInfo)
				.orElseThrow(NotFoundException::new);
	}

	public StarshipInfo getStarship(String number) {
		return starshipDao.getByNumber(number)
				.map(this::createStarshipInfo)
				.orElseThrow(NotFoundException::new);
	}

}
