package ru.zubmike.service.demo.dao;

import ru.zubmike.core.dao.EntityItemDao;
import ru.zubmike.service.demo.types.Starship;

import java.util.Optional;

public interface StarshipDao extends EntityItemDao<Long, Starship> {

	Optional<Starship> getByNumber(String number);

}
