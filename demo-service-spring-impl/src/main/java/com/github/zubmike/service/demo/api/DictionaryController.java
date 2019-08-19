package com.github.zubmike.service.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.zubmike.core.types.BasicDictItem;
import com.github.zubmike.service.demo.services.DictionaryService;

import java.util.List;

@RestController
@RequestMapping(value = "/dictionaries", produces = MediaType.APPLICATION_JSON_VALUE)
public class DictionaryController {

	private final DictionaryService dictionaryService;

	@Autowired
	public DictionaryController(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	@GetMapping("/planetary-systems")
	public List<BasicDictItem> getPlanetarySystems() {
		return dictionaryService.getPlanetarySystems();
	}
}
