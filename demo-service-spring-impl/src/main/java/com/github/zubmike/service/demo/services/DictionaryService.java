package com.github.zubmike.service.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.zubmike.core.types.BasicDictItem;
import com.github.zubmike.core.types.DictItem;
import com.github.zubmike.service.demo.dao.PlanetarySystemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

	private final PlanetarySystemRepository planetarySystemRepository;

	@Autowired
	public DictionaryService(PlanetarySystemRepository planetarySystemRepository) {
		this.planetarySystemRepository = planetarySystemRepository;
	}

	public List<BasicDictItem> getPlanetarySystems() {
		return planetarySystemRepository.findAll()
				.stream()
				.map(DictionaryService::createBasicDictItem)
				.collect(Collectors.toList());
	}

	private static BasicDictItem createBasicDictItem(DictItem<Integer> item) {
		return new BasicDictItem(item.getId(), item.getName());
	}

}
