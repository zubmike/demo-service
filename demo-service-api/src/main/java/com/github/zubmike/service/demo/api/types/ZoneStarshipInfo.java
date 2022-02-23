package com.github.zubmike.service.demo.api.types;

import java.io.Serial;

public class ZoneStarshipInfo extends StarshipEntry {

	@Serial
	private static final long serialVersionUID = -8145324767333957156L;

	private long id;
	private String parkDate;

	public ZoneStarshipInfo() {
	}

	public ZoneStarshipInfo(long id, String number, String parkDate) {
		super(number);
		this.id = id;
		this.parkDate = parkDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParkDate() {
		return parkDate;
	}

	public void setParkDate(String parkDate) {
		this.parkDate = parkDate;
	}
}
