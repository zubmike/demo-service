package com.github.zubmike.service.demo.logic;

import com.github.zubmike.core.types.BasicDictItem;
import com.github.zubmike.core.types.DictItem;
import com.github.zubmike.service.demo.dao.PlanetarySystemDao;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryLogic {

	private final PlanetarySystemDao planetarySystemDao;

	@Inject
	public DictionaryLogic(PlanetarySystemDao planetarySystemDao) {
		this.planetarySystemDao = planetarySystemDao;
	}

	public List<BasicDictItem> getPlanetarySystems() {
		return planetarySystemDao.getAll()
				.stream()
				.map(DictionaryLogic::createBasicDictItem)
				.collect(Collectors.toList());
	}

	private static BasicDictItem createBasicDictItem(DictItem<Integer> item) {
		return new BasicDictItem(item.getId(), item.getName());
	}

}
