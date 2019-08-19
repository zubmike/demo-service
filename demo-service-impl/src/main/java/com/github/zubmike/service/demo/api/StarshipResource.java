package com.github.zubmike.service.demo.api;

import org.glassfish.jersey.server.ResourceConfig;
import com.github.zubmike.service.demo.api.types.StarshipEntry;
import com.github.zubmike.service.demo.api.types.StarshipInfo;
import com.github.zubmike.service.demo.logic.StarshipLogic;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/starships")
public class StarshipResource extends BasicResource {

	private final StarshipLogic starshipLogic;

	@Inject
	public StarshipResource(ResourceConfig resourceConfig, StarshipLogic starshipLogic) {
		super(resourceConfig);
		this.starshipLogic = starshipLogic;
	}

	@POST
	public StarshipInfo addStarship(StarshipEntry starshipEntry) {
		return starshipLogic.addStarship(starshipEntry);
	}

	@GET
	@Path("/{id}")
	public StarshipInfo getStarship(@PathParam("id") long id) {
		return starshipLogic.getStarship(id);
	}

	@GET
	@Path("/number/{number}")
	public StarshipInfo getStarship(@PathParam("number") String number) {
		return starshipLogic.getStarship(number);
	}

}
