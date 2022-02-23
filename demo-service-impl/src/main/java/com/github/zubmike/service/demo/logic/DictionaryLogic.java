package com.github.zubmike.service.demo.logic;

import com.github.zubmike.core.types.BasicDictItem;
import com.github.zubmike.core.utils.DictItemUtils;
import com.github.zubmike.service.demo.dao.PlanetarySystemDao;

import javax.inject.Inject;
import java.util.List;

public class DictionaryLogic {

	private final PlanetarySystemDao planetarySystemDao;

	@Inject
	public DictionaryLogic(PlanetarySystemDao planetarySystemDao) {
		this.planetarySystemDao = planetarySystemDao;
	}

	public List<BasicDictItem> getPlanetarySystems() {
		return DictItemUtils.createBasicDictItems(planetarySystemDao.getAll());
	}

}
