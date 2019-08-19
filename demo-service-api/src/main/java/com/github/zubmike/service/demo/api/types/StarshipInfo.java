package com.github.zubmike.service.demo.api.types;

public class StarshipInfo extends StarshipEntry {

	private static final long serialVersionUID = 1L;

	private long id;
	private int planetarySystemId;
	private String planetarySystemName;
	private String createDate;
	private int timeCount;

	public StarshipInfo() {
	}

	public StarshipInfo(long id, String number, int planetarySystemId, String planetarySystemName, String createDate, int timeCount) {
		super(number);
		this.id = id;
		this.planetarySystemId = planetarySystemId;
		this.planetarySystemName = planetarySystemName;
		this.createDate = createDate;
		this.timeCount = timeCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPlanetarySystemId() {
		return planetarySystemId;
	}

	public void setPlanetarySystemId(int planetarySystemId) {
		this.planetarySystemId = planetarySystemId;
	}

	public String getPlanetarySystemName() {
		return planetarySystemName;
	}

	public void setPlanetarySystemName(String planetarySystemName) {
		this.planetarySystemName = planetarySystemName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getTimeCount() {
		return timeCount;
	}

	public void setTimeCount(int timeCount) {
		this.timeCount = timeCount;
	}
}
