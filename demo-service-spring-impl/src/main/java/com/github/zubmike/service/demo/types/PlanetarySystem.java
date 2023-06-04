package com.github.zubmike.service.demo.types;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;

@Entity
@Table(name = "planetary_systems")
@Immutable
@Cache(region = "planetarySystem", usage = CacheConcurrencyStrategy.READ_ONLY)
public class PlanetarySystem extends BasicEntityDictItem {

	@Serial
	private static final long serialVersionUID = 7334333138985271731L;

	@Column(name = "code")
	private String code;

	public PlanetarySystem() {
	}

	public PlanetarySystem(int id, String name, String code) {
		super(id, name);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
