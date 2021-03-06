package com.github.zubmike.service.demo.api;

import org.glassfish.jersey.server.ResourceConfig;
import com.github.zubmike.core.types.BasicDictItem;
import com.github.zubmike.service.demo.logic.DictionaryLogic;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/dictionaries")
public class DictionaryResource extends BasicResource {

	private DictionaryLogic dictionaryLogic;

	@Inject
	public DictionaryResource(ResourceConfig resourceConfig, DictionaryLogic dictionaryLogic) {
		super(resourceConfig);
		this.dictionaryLogic = dictionaryLogic;
	}

	@GET
	@Path("/planetary-systems")
	public List<BasicDictItem> getPlanetarySystems() {
		return dictionaryLogic.getPlanetarySystems();
	}

}
