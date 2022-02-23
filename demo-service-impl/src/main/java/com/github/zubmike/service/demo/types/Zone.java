package com.github.zubmike.service.demo.types;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.github.zubmike.service.types.BasicEntityDictItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;

@Entity
@Table(name = "zones")
@Cache(region = "zone", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Zone extends BasicEntityDictItem {

	@Serial
	private static final long serialVersionUID = 5703708337259726084L;

	@Column(name = "max_size")
	private int maxSize;

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
