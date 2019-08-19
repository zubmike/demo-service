package com.github.zubmike.service.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.zubmike.service.demo.api.types.StarshipEntry;
import com.github.zubmike.service.demo.api.types.StarshipInfo;
import com.github.zubmike.service.demo.services.StarshipService;

@RestController
@RequestMapping(value = "/starships", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class StarshipController {

	private final StarshipService starshipService;

	@Autowired
	public StarshipController(StarshipService starshipService) {
		this.starshipService = starshipService;
	}

	@PostMapping
	public StarshipInfo addStarship(@RequestBody StarshipEntry starshipEntry) {
		return starshipService.addStarship(starshipEntry);
	}

	@GetMapping("/{id}")
	public StarshipInfo getStarship(@PathVariable("id") long id) {
		return starshipService.getStarship(id);
	}

	@GetMapping("/number/{number}")
	public StarshipInfo getStarship(@PathVariable("number") String number) {
		return starshipService.getStarship(number);
	}

}
