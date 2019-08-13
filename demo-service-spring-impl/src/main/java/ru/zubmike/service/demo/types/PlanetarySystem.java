package ru.zubmike.service.demo.types;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "planetary_systems")
@Immutable
@Cache(region = "planetarySystem", usage = CacheConcurrencyStrategy.READ_ONLY)
public class PlanetarySystem extends BasicEntityDictItem {

	private static final long serialVersionUID = 1L;

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
