package ru.zubmike.service.demo.dao;

import ru.zubmike.core.dao.DictItemDao;
import ru.zubmike.service.demo.types.PlanetarySystem;

import java.util.Optional;

public interface PlanetarySystemDao extends DictItemDao<Integer, PlanetarySystem> {

	Optional<PlanetarySystem> getByCode(String code);

}
