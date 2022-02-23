package com.github.zubmike.service.demo.api;

import com.github.zubmike.service.demo.api.types.ZoneEntry;
import com.github.zubmike.service.demo.api.types.ZoneInfo;
import com.github.zubmike.service.demo.api.types.ZoneStarshipInfo;
import com.github.zubmike.service.demo.logic.ZoneLogic;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Secure
@Path("/zones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ZoneResource {

	private final ZoneLogic zoneLogic;

	@Inject
	public ZoneResource(ZoneLogic zoneLogic) {
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
	public Response addToZone(@PathParam("id") int id,
	                          @PathParam("starship-id") long starshipId) {
		zoneLogic.addToZone(id, starshipId);
		return Response.ok().build();
	}

	@DELETE
	@Path("/{id}/starships/{starship-id}")
	public Response deleteFromZone(@PathParam("id") int id,
	                               @PathParam("starship-id") long starshipId) {
		zoneLogic.deleteFromZone(id, starshipId);
		return Response.ok().build();
	}

	@GET
	@Path("/{id}/starships")
	public List<ZoneStarshipInfo> getZoneStarships(@PathParam("id") int id) {
		return zoneLogic.getZoneStarships(id);
	}
}
