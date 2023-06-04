package com.github.zubmike.service.demo.types;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "zone_spaces")
public class ZoneSpace implements Serializable {

	@Serial
	private static final long serialVersionUID = -6769918792569542867L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "zone_id")
	private int zoneId;

	@Column(name = "starship_id")
	private long starshipId;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public long getStarshipId() {
		return starshipId;
	}

	public void setStarshipId(long starshipId) {
		this.starshipId = starshipId;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
}
