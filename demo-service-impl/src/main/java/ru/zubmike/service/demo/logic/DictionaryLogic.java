package ru.zubmike.service.demo.logic;

import ru.zubmike.core.types.BasicDictItem;
import ru.zubmike.core.types.DictItem;
import ru.zubmike.service.demo.dao.PlanetarySystemDao;

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
