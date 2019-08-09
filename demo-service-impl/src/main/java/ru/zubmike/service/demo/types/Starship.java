package ru.zubmike.service.demo.types;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "starships")
public class Starship {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "number")
	private String number;

	@Column(name = "planetary_system_id")
	private int planetarySystemId;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	@Column(name = "time_count")
	private int timeCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getPlanetarySystemId() {
		return planetarySystemId;
	}

	public void setPlanetarySystemId(int planetarySystemId) {
		this.planetarySystemId = planetarySystemId;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public int getTimeCount() {
		return timeCount;
	}

	public void setTimeCount(int timeCount) {
		this.timeCount = timeCount;
	}
}
