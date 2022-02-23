package com.github.zubmike.service.demo.dao;

import com.github.zubmike.service.dao.EntityItemDao;
import com.github.zubmike.service.demo.types.Starship;

import java.util.Optional;

public interface StarshipDao extends EntityItemDao<Long, Starship> {

	Optional<Starship> getByNumber(String number);

}
