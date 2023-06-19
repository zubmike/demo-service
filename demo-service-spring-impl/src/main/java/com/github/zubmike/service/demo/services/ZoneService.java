package com.github.zubmike.service.demo.services;

import com.github.zubmike.core.utils.DateTimeUtils;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.api.types.ZoneEntry;
import com.github.zubmike.service.demo.api.types.ZoneInfo;
import com.github.zubmike.service.demo.api.types.ZoneStarshipInfo;
import com.github.zubmike.service.demo.dao.StarshipRepository;
import com.github.zubmike.service.demo.dao.ZoneRepository;
import com.github.zubmike.service.demo.dao.ZoneSpaceRepository;
import com.github.zubmike.service.demo.types.ServiceUserContext;
import com.github.zubmike.service.demo.types.Starship;
import com.github.zubmike.service.demo.types.Zone;
import com.github.zubmike.service.demo.types.ZoneSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class ZoneService extends UserContextService {

	private static final int MIN_SIZE = 2;

	private final ZoneRepository zoneRepository;
	private final ZoneSpaceRepository zoneSpaceRepository;
	private final StarshipRepository starshipRepository;

	@Autowired
	public ZoneService(ServiceUserContext serviceUserContext, ZoneRepository zoneRepository,
					   ZoneSpaceRepository zoneSpaceRepository, StarshipRepository starshipRepository) {
		super(serviceUserContext);
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
			throw new InvalidParameterException(ServiceResource.getString("res.string.starshipParked", starship.getNumber()));
		});
		int usedSize = zoneSpaceRepository.findAllByZoneId(zone.getId()).size();
		if (usedSize >= zone.getMaxSize()) {
			throw new InvalidParameterException(ServiceResource.getString(serviceUserContext, "res.string.noEmptySpace"));
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
				.orElseThrow(() -> {
					var message = ServiceResource.getString(serviceUserContext, "res.string.starshipNotParked", starship.getNumber(), zone.getName());
					return new InvalidParameterException(message);
				});
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
