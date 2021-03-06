package com.github.zubmike.service.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.zubmike.service.demo.types.PlanetarySystem;

import java.util.Optional;

@Repository
public interface PlanetarySystemRepository extends JpaRepository<PlanetarySystem, Integer> {

	Optional<PlanetarySystem> findByCode(String code);

}
