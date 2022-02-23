package com.github.zubmike.service.demo.api;

import com.github.zubmike.core.types.BasicDictItem;
import com.github.zubmike.service.demo.logic.DictionaryLogic;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/dictionaries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DictionaryResource {

	private final DictionaryLogic dictionaryLogic;

	@Inject
	public DictionaryResource(DictionaryLogic dictionaryLogic) {
		this.dictionaryLogic = dictionaryLogic;
	}

	@GET
	@Path("/planetary-systems")
	public List<BasicDictItem> getPlanetarySystems() {
		return dictionaryLogic.getPlanetarySystems();
	}

}
