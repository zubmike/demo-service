package ru.zubmike.service.demo.api;

import org.glassfish.jersey.server.ResourceConfig;
import ru.zubmike.service.demo.api.types.ZoneStarshipInfo;
import ru.zubmike.service.demo.api.types.ZoneEntry;
import ru.zubmike.service.demo.api.types.ZoneInfo;
import ru.zubmike.service.demo.logic.ZoneLogic;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/zones")
public class ZoneResource extends BasicResource {

	private final ZoneLogic zoneLogic;

	@Inject
	public ZoneResource(ResourceConfig resourceConfig, ZoneLogic zoneLogic) {
		super(resourceConfig);
		this.zoneLogic = zoneLogic;
	}

	@POST
	public ZoneInfo addZone(ZoneEntry zoneEntry) {
		return zoneLogic.addZone(zoneEntry);
	}

	@GET
	public List<ZoneInfo> getZones() {
		return zoneLogic.getZones();
	}

	@GET
	@Path("/{id}")
	public ZoneInfo getZone(@PathParam("id") int id) {
		return zoneLogic.getZone(id);
	}

	@POST
	@Path("/{id}/starships/{starship-id}")
	public void addToZone(@PathParam("id") int id,
	                      @PathParam("starship-id") long starshipId) {
		zoneLogic.addToZone(id, starshipId);
	}

	@DELETE
	@Path("/{id}/starships/{starship-id}")
	public void deleteFromZone(@PathParam("id") int id,
	                           @PathParam("starship-id") long starshipId) {
		zoneLogic.deleteFromZone(id, starshipId);
	}

	@GET
	@Path("/{id}/starships")
	public List<ZoneStarshipInfo> getZoneStarships(@PathParam("id") int id) {
		return zoneLogic.getZoneStarships(id);
	}
}
