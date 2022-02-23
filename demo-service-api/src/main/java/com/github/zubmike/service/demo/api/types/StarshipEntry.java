package com.github.zubmike.service.demo.api.types;

import java.io.Serial;
import java.io.Serializable;

public class StarshipEntry implements Serializable {

	@Serial
	private static final long serialVersionUID = -7275731517621850250L;

	private String number;

	public StarshipEntry() {
	}

	public StarshipEntry(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
