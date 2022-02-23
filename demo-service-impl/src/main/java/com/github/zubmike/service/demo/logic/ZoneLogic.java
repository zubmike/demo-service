package com.github.zubmike.service.demo.logic;

import com.github.zubmike.core.utils.DateTimeUtils;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.api.types.ZoneStarshipInfo;
import com.github.zubmike.service.demo.api.types.ZoneEntry;
import com.github.zubmike.service.demo.api.types.ZoneInfo;
import com.github.zubmike.service.demo.dao.StarshipDao;
import com.github.zubmike.service.demo.dao.ZoneDao;
import com.github.zubmike.service.demo.dao.ZoneSpaceDao;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.demo.types.Starship;
import com.github.zubmike.service.demo.types.Zone;
import com.github.zubmike.service.demo.types.ZoneSpace;
import com.github.zubmike.service.managers.TransactionManager;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneLogic {

	private static final int MIN_SIZE = 2;

	private final ServiceUserContext serviceUserContext;

	private final ZoneDao zoneDao;
	private final ZoneSpaceDao zoneSpaceDao;
	private final StarshipDao starshipDao;

	private final TransactionManager transactionManager;

	@Inject
	public ZoneLogic(ServiceUserContext serviceUserContext, ZoneDao zoneDao, ZoneSpaceDao zoneSpaceDao, StarshipDao starshipDao,
	                 TransactionManager transactionManager) {
		this.serviceUserContext = serviceUserContext;
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

	private void checkZoneEntry(ZoneEntry zoneEntry) {
		if (zoneEntry.getName() == null || zoneEntry.getName().isEmpty()) {
			throw new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.invalidName"));
		}
		if (zoneEntry.getMaxSize() < MIN_SIZE) {
			throw new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.invalidSize"));
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
			throw new InvalidParameterException(ServiceResource.getString("res.string.starshipParked", starship.getNumber()));
		});
		int usedSize = zoneSpaceDao.getAllByZone(zone.getId()).size();
		if (usedSize >= zone.getMaxSize()) {
			throw new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.noEmptySpace"));
		}
	}

	private static ZoneSpace createZoneSpace(Zone zone, Starship starship) {
		var zoneSpace = new ZoneSpace();
		zoneSpace.setZoneId(zone.getId());
		zoneSpace.setStarshipId(starship.getId());
		zoneSpace.setCreateDate(LocalDateTime.now());
		return zoneSpace;
	}

	public void deleteFromZone(int zoneId, long starshipId) {
		var zone = zoneDao.get(zoneId).orElseThrow(NotFoundException::new);
		var starship = starshipDao.get(starshipId).orElseThrow(NotFoundException::new);
		var zoneSpace = getZoneSpace(zone, starship);
		zoneSpaceDao.remove(zoneSpace);
	}

	private ZoneSpace getZoneSpace(Zone zone, Starship starship) {
		return zoneSpaceDao.getUsedSpace(zone.getId(), starship.getId())
				.orElseThrow(() -> {
					var string = ServiceResource.getString(serviceUserContext, "res.string.starshipNotParked", starship.getNumber(), zone.getName());
					return new InvalidParameterException(string);
				});
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
