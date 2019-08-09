package ru.zubmike.service.demo.api;

import org.glassfish.jersey.server.ResourceConfig;
import ru.zubmike.service.demo.api.types.ZoneEntry;
import ru.zubmike.service.demo.api.types.ZoneInfo;
import ru.zubmike.service.demo.logic.ZoneLogic;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/zones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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

}
