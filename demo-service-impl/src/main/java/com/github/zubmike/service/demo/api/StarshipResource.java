package com.github.zubmike.service.demo.api;

import com.github.zubmike.service.demo.api.types.StarshipEntry;
import com.github.zubmike.service.demo.api.types.StarshipInfo;
import com.github.zubmike.service.demo.logic.StarshipLogic;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Secure
@Path("/starships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StarshipResource {

	private final StarshipLogic starshipLogic;

	@Inject
	public StarshipResource(StarshipLogic starshipLogic) {
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
