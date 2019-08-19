package com.github.zubmike.service.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.zubmike.service.demo.types.ZoneSpace;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneSpaceRepository extends JpaRepository<ZoneSpace, Long> {

	List<ZoneSpace> findAllByZoneId(int zoneId);

	Optional<ZoneSpace> findByStarshipId(long starshipId);

	@Query("select zs from ZoneSpace zs where zs.zoneId = :zoneId and zs.starshipId = :starshipId")
	Optional<ZoneSpace> getUsedSpace(@Param("zoneId") int zoneId, @Param("starshipId") long starshipId);

}
