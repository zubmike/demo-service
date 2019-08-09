package ru.zubmike.service.demo.logic;

import ru.zubmike.core.utils.InvalidParameterException;
import ru.zubmike.core.utils.NotFoundException;
import ru.zubmike.service.demo.api.types.ZoneEntry;
import ru.zubmike.service.demo.api.types.ZoneInfo;
import ru.zubmike.service.demo.dao.ZoneDao;
import ru.zubmike.service.demo.types.Zone;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneLogic {

	private static final int MIN_SIZE = 2;

	private final ZoneDao zoneDao;

	@Inject
	public ZoneLogic(ZoneDao zoneDao) {
		this.zoneDao = zoneDao;
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

}
