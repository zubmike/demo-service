package ru.zubmike.service.demo.api.types;

public class ZoneStarshipInfo extends StarshipEntry {

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
