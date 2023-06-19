package com.github.zubmike.service.demo.services;

import com.github.zubmike.core.types.DictItem;
import com.github.zubmike.core.utils.DateTimeUtils;
import com.github.zubmike.core.utils.DuplicateException;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.api.types.StarshipEntry;
import com.github.zubmike.service.demo.api.types.StarshipInfo;
import com.github.zubmike.service.demo.dao.PlanetarySystemRepository;
import com.github.zubmike.service.demo.dao.StarshipRepository;
import com.github.zubmike.service.demo.types.PlanetarySystem;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.demo.types.Starship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequestScope
public class StarshipService extends UserContextService {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^([A-Z]{3})-([0-9]{6})$");

	private final StarshipRepository starshipRepository;
	private final PlanetarySystemRepository planetarySystemRepository;

	@Autowired
	public StarshipService(ServiceUserContext serviceUserContext, StarshipRepository starshipRepository,
						   PlanetarySystemRepository planetarySystemRepository) {
		super(serviceUserContext);
		this.starshipRepository = starshipRepository;
		this.planetarySystemRepository = planetarySystemRepository;
	}

	public StarshipInfo addStarship(StarshipEntry starshipEntry) {
		var planetarySystem = parsePlanetarySystem(starshipEntry);
		starshipRepository.findByNumber(starshipEntry.getNumber()).ifPresent(it -> {
			throw new DuplicateException(ServiceResource.getString(serviceUserContext, "res.string.duplicate"));
		});
		var starship = new Starship();
		starship.setNumber(starshipEntry.getNumber());
		starship.setPlanetarySystemId(planetarySystem.getId());
		starship.setCreateDate(LocalDateTime.now());
		starship.setTimeCount(0);
		starshipRepository.save(starship);
		return createStarshipInfo(starship);
	}

	private PlanetarySystem parsePlanetarySystem(StarshipEntry starshipEntry) {
		var matcher =  NUMBER_PATTERN.matcher(starshipEntry.getNumber());
		if (!matcher.find() || matcher.groupCount() < 2) {
			throw new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.invalidNumberFormat"));
		}
		return planetarySystemRepository.findByCode(matcher.group(1))
				.orElseThrow(() -> new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.unknownPlanetarySystem")));
	}

	private StarshipInfo createStarshipInfo(Starship starship) {
		return new StarshipInfo(
				starship.getId(),
				starship.getNumber(),
				starship.getPlanetarySystemId(),
				planetarySystemRepository.findById(starship.getPlanetarySystemId()).map(DictItem::getName).orElse(null),
				DateTimeUtils.toString(starship.getCreateDate()),
				starship.getTimeCount()
		);
	}

	public StarshipInfo getStarship(long id) {
		return starshipRepository.findById(id)
				.map(this::createStarshipInfo)
				.orElseThrow(NotFoundException::new);
	}

	public StarshipInfo getStarship(String number) {
		return starshipRepository.findByNumber(number)
				.map(this::createStarshipInfo)
				.orElseThrow(NotFoundException::new);
	}

}
