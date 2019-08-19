package com.github.zubmike.service.demo.api.types;

import java.io.Serializable;

public class StarshipEntry implements Serializable {

	private static final long serialVersionUID = 1L;

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
