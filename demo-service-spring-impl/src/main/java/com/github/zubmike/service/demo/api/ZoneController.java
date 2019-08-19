package com.github.zubmike.service.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.zubmike.service.demo.api.types.ZoneEntry;
import com.github.zubmike.service.demo.api.types.ZoneInfo;
import com.github.zubmike.service.demo.api.types.ZoneStarshipInfo;
import com.github.zubmike.service.demo.services.ZoneService;

import java.util.List;

@RestController
@RequestMapping(value = "/zones", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ZoneController {

	private final ZoneService zoneService;

	@Autowired
	public ZoneController(ZoneService zoneService) {
		this.zoneService = zoneService;
	}

	@PostMapping
	public ZoneInfo addZone(ZoneEntry zoneEntry) {
		return zoneService.addZone(zoneEntry);
	}

	@GetMapping
	public List<ZoneInfo> getZones() {
		return zoneService.getZones();
	}

	@GetMapping("/{id}")
	public ZoneInfo getZone(@PathVariable("id") int id) {
		return zoneService.getZone(id);
	}

	@PostMapping("/{id}/starships/{starship-id}")
	public void addToZone(@PathVariable("id") int id,
	                      @PathVariable("starship-id") long starshipId) {
		zoneService.addToZone(id, starshipId);
	}

	@DeleteMapping("/{id}/starships/{starship-id}")
	public void deleteFromZone(@PathVariable("id") int id,
	                           @PathVariable("starship-id") long starshipId) {
		zoneService.deleteFromZone(id, starshipId);
	}

	@GetMapping("/{id}/starships")
	public List<ZoneStarshipInfo> getZoneStarships(@PathVariable("id") int id) {
		return zoneService.getZoneStarships(id);
	}

}
