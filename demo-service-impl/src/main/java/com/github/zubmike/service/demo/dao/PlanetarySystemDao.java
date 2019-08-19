package com.github.zubmike.service.demo.dao;

import com.github.zubmike.core.dao.DictItemDao;
import com.github.zubmike.service.demo.types.PlanetarySystem;

import java.util.Optional;

public interface PlanetarySystemDao extends DictItemDao<Integer, PlanetarySystem> {

	Optional<PlanetarySystem> getByCode(String code);

}
