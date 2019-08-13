package ru.zubmike.service.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zubmike.core.utils.DateTimeUtils;
import ru.zubmike.core.utils.InvalidParameterException;
import ru.zubmike.core.utils.NotFoundException;
import ru.zubmike.service.demo.api.types.ZoneEntry;
import ru.zubmike.service.demo.api.types.ZoneInfo;
import ru.zubmike.service.demo.api.types.ZoneStarshipInfo;
import ru.zubmike.service.demo.dao.StarshipRepository;
import ru.zubmike.service.demo.dao.ZoneRepository;
import ru.zubmike.service.demo.dao.ZoneSpaceRepository;
import ru.zubmike.service.demo.types.Starship;
import ru.zubmike.service.demo.types.Zone;
import ru.zubmike.service.demo.types.ZoneSpace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneService {

	private static final int MIN_SIZE = 2;

	private final ZoneRepository zoneRepository;
	private final ZoneSpaceRepository zoneSpaceRepository;
	private final StarshipRepository starshipRepository;

	@Autowired
	public ZoneService(ZoneRepository zoneRepository, ZoneSpaceRepository zoneSpaceRepository, StarshipRepository starshipRepository) {
		this.zoneRepository = zoneRepository;
		this.zoneSpaceRepository = zoneSpaceRepository;
		this.starshipRepository = starshipRepository;
	}

	public ZoneInfo addZone(ZoneEntry zoneEntry) {
		checkZoneEntry(zoneEntry);

		Zone zone = new Zone();
		zone.setName(zoneEntry.getName());
		zone.setMaxSize(zoneEntry.getMaxSize());

		zoneRepository.save(zone);

		return createZoneInfo(zone);
	}

	private static void checkZoneEntry(ZoneEntry zoneEntry) {
		if (zoneEntry.getName() == null || zoneEntry.getName().isEmpty()) {
			throw new InvalidParameterException("Invalid name");
		}
		if (zoneEntry.getMaxSize() < MIN_SIZE) {
			throw new InvalidParameterException("Invalid size");
		}
	}

	private static ZoneInfo createZoneInfo(Zone zone) {
		return new ZoneInfo(
			zone.getId(),
			zone.getName(),
			zone.getMaxSize());
	}

	public List<ZoneInfo> getZones() {
		return zoneRepository.findAll()
				.stream()
				.map(ZoneService::createZoneInfo)
				.collect(Collectors.toList());
	}

	public ZoneInfo getZone(int id) {
		return zoneRepository.findById(id)
				.map(ZoneService::createZoneInfo)
				.orElseThrow(NotFoundException::new);
	}

	@Transactional
	public void addToZone(int zoneId, long starshipId) {
		Zone zone = zoneRepository.findById(zoneId).orElseThrow(NotFoundException::new);
		Starship starship = starshipRepository.findById(starshipId).orElseThrow(NotFoundException::new);
		checkFreeZoneSpace(zone, starship);
		ZoneSpace zoneSpace = createZoneSpace(zone, starship);
		zoneSpaceRepository.save(zoneSpace);
		starship.setTimeCount(starship.getTimeCount() + 1);
		starshipRepository.save(starship);
	}

	private void checkFreeZoneSpace(Zone zone, Starship starship) {
		zoneSpaceRepository.findByStarshipId(starship.getId()).ifPresent(item -> {
			throw new InvalidParameterException("Starship is already parked");
		});
		int usedSize = zoneSpaceRepository.findAllByZoneId(zone.getId()).size();
		if (usedSize >= zone.getMaxSize()) {
			throw new InvalidParameterException("Not found empty space");
		}
	}

	private static ZoneSpace createZoneSpace(Zone zone, Starship starship) {
		ZoneSpace zoneSpace = new ZoneSpace();
		zoneSpace.setZoneId(zone.getId());
		zoneSpace.setStarshipId(starship.getId());
		zoneSpace.setCreateDate(LocalDateTime.now());
		return zoneSpace;
	}

	public void deleteFromZone(int zoneId, long starshipId) {
		Zone zone = zoneRepository.findById(zoneId).orElseThrow(NotFoundException::new);
		Starship starship = starshipRepository.findById(starshipId).orElseThrow(NotFoundException::new);
		ZoneSpace zoneSpace = getZoneSpace(zone, starship);
		zoneSpaceRepository.delete(zoneSpace);
	}

	private ZoneSpace getZoneSpace(Zone zone, Starship starship) {
		return zoneSpaceRepository.getUsedSpace(zone.getId(), starship.getId())
				.orElseThrow(() -> new InvalidParameterException("Starship is not parked in the zone"));
	}

	public List<ZoneStarshipInfo> getZoneStarships(int zoneId) {
		zoneRepository.findById(zoneId).orElseThrow(NotFoundException::new);
		return zoneSpaceRepository.findAllByZoneId(zoneId)
				.stream()
				.map(this::createParkedStarshipInfo)
				.collect(Collectors.toList());
	}

	private ZoneStarshipInfo createParkedStarshipInfo(ZoneSpace item) {
		return new ZoneStarshipInfo(
				item.getStarshipId(),
				starshipRepository.findById(item.getStarshipId())
						.map(Starship::getNumber)
						.orElse(null),
				DateTimeUtils.toString(item.getCreateDate()));
	}
}
