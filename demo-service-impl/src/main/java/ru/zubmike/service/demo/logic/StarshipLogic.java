package ru.zubmike.service.demo.logic;

import ru.zubmike.core.utils.DateTimeUtils;
import ru.zubmike.core.utils.InvalidParameterException;
import ru.zubmike.core.utils.NotFoundException;
import ru.zubmike.service.demo.api.types.StarshipEntry;
import ru.zubmike.service.demo.api.types.StarshipInfo;
import ru.zubmike.service.demo.dao.PlanetarySystemDao;
import ru.zubmike.service.demo.dao.StarshipDao;
import ru.zubmike.service.demo.types.PlanetarySystem;
import ru.zubmike.service.demo.types.Starship;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class StarshipLogic {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^([A-Z]{3})-([0-9]{6})$");

	private final StarshipDao starshipDao;
	private final PlanetarySystemDao planetarySystemDao;

	@Inject
	public StarshipLogic(StarshipDao starshipDao, PlanetarySystemDao planetarySystemDao) {
		this.starshipDao = starshipDao;
		this.planetarySystemDao = planetarySystemDao;
	}

	public StarshipInfo addStarship(StarshipEntry starshipEntry) {
		PlanetarySystem planetarySystem = parsePlanetarySystem(starshipEntry);
		starshipDao.getByNumber(starshipEntry.getNumber()).ifPresent(it -> {
			throw new InvalidParameterException("Duplicate starship");
		});
		Starship starship = new Starship();
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
			throw new InvalidParameterException("Invalid number format");
		}
		return planetarySystemDao.getByCode(matcher.group(1))
				.orElseThrow(() -> new InvalidParameterException("Unknown planetary system"));
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
