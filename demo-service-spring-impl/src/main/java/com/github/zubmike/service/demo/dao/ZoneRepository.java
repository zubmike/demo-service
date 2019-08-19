package com.github.zubmike.service.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.zubmike.service.demo.types.Zone;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer> {

}
