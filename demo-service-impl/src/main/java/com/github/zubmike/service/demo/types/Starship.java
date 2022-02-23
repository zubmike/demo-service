package com.github.zubmike.service.demo.types;

import com.github.zubmike.core.types.EntityItem;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "starships")
@Cache(region = "zone", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Starship implements EntityItem<Long> {

	@Serial
	private static final long serialVersionUID = -6222734621829538476L;

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

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
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
