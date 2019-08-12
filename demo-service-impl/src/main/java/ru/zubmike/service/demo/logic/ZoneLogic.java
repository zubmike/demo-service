package ru.zubmike.service.demo.logic;

import ru.zubmike.core.utils.DateTimeUtils;
import ru.zubmike.core.utils.InvalidParameterException;
import ru.zubmike.core.utils.NotFoundException;
import ru.zubmike.service.demo.api.types.ZoneStarshipInfo;
import ru.zubmike.service.demo.api.types.ZoneEntry;
import ru.zubmike.service.demo.api.types.ZoneInfo;
import ru.zubmike.service.demo.dao.StarshipDao;
import ru.zubmike.service.demo.dao.ZoneDao;
import ru.zubmike.service.demo.dao.ZoneSpaceDao;
import ru.zubmike.service.demo.types.Starship;
import ru.zubmike.service.demo.types.Zone;
import ru.zubmike.service.demo.types.ZoneSpace;
import ru.zubmike.service.managers.TransactionManager;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneLogic {

	private static final int MIN_SIZE = 2;

	private final ZoneDao zoneDao;
	private final ZoneSpaceDao zoneSpaceDao;
	private final StarshipDao starshipDao;

	private final TransactionManager transactionManager;

	@Inject
	public ZoneLogic(ZoneDao zoneDao, ZoneSpaceDao zoneSpaceDao, StarshipDao starshipDao,
	                 TransactionManager transactionManager
	) {
		this.zoneDao = zoneDao;
		this.zoneSpaceDao = zoneSpaceDao;
		this.starshipDao = starshipDao;
		this.transactionManager = transactionManager;
	}

	public ZoneInfo addZone(ZoneEntry zoneEntry) {
		checkZoneEntry(zoneEntry);

		Zone zone = new Zone();
		zone.setName(zoneEntry.getName());
		zone.setMaxSize(zoneEntry.getMaxSize());

		zoneDao.add(zone);

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
		return zoneDao.getAll()
				.stream()
				.map(ZoneLogic::createZoneInfo)
				.collect(Collectors.toList());
	}

	public ZoneInfo getZone(int id) {
		return zoneDao.get(id)
				.map(ZoneLogic::createZoneInfo)
				.orElseThrow(NotFoundException::new);
	}

	public void addToZone(int zoneId, long starshipId) {
		transactionManager.run(() -> {
			Zone zone = zoneDao.get(zoneId).orElseThrow(NotFoundException::new);
			Starship starship = starshipDao.get(starshipId).orElseThrow(NotFoundException::new);
			checkFreeZoneSpace(zone, starship);
			ZoneSpace zoneSpace = createZoneSpace(zone, starship);
			zoneSpaceDao.add(zoneSpace);
			starship.setTimeCount(starship.getTimeCount() + 1);
			starshipDao.update(starship);
		});
	}

	private void checkFreeZoneSpace(Zone zone, Starship starship) {
		zoneSpaceDao.getByStarship(starship.getId()).ifPresent(item -> {
			throw new InvalidParameterException("Starship is already parked");
		});
		int usedSize = zoneSpaceDao.getAllByZone(zone.getId()).size();
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
		Zone zone = zoneDao.get(zoneId).orElseThrow(NotFoundException::new);
		Starship starship = starshipDao.get(starshipId).orElseThrow(NotFoundException::new);
		ZoneSpace zoneSpace = getZoneSpace(zone, starship);
		zoneSpaceDao.remove(zoneSpace);
	}

	private ZoneSpace getZoneSpace(Zone zone, Starship starship) {
		return zoneSpaceDao.getUsedSpace(zone.getId(), starship.getId())
				.orElseThrow(() -> new InvalidParameterException("Starship is not parked in the zone"));
	}

	public List<ZoneStarshipInfo> getZoneStarships(int zoneId) {
		zoneDao.get(zoneId).orElseThrow(NotFoundException::new);
		return zoneSpaceDao.getAllByZone(zoneId)
				.stream()
				.map(this::createParkedStarshipInfo)
				.collect(Collectors.toList());
	}

	private ZoneStarshipInfo createParkedStarshipInfo(ZoneSpace item) {
		return new ZoneStarshipInfo(
				item.getStarshipId(),
				starshipDao.get(item.getStarshipId())
						.map(Starship::getNumber)
						.orElse(null),
				DateTimeUtils.toString(item.getCreateDate()));
	}
}
