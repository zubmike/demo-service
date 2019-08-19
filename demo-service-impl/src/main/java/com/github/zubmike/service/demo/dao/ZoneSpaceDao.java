package com.github.zubmike.service.demo.dao;

import com.github.zubmike.core.dao.EntityItemDao;
import com.github.zubmike.service.demo.types.ZoneSpace;

import java.util.List;
import java.util.Optional;

public interface ZoneSpaceDao extends EntityItemDao<Long, ZoneSpace> {

	List<ZoneSpace> getAllByZone(int zoneId);

	Optional<ZoneSpace> getByStarship(long starshipId);

	Optional<ZoneSpace> getUsedSpace(int zoneId, long starshipId);

}
