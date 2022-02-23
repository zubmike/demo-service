package com.github.zubmike.service.demo.api.types;

import java.io.Serial;
import java.io.Serializable;

public class ZoneEntry implements Serializable {

	@Serial
	private static final long serialVersionUID = -8745015981066708613L;

	private String name;
	private int maxSize;

	public ZoneEntry() {
	}

	public ZoneEntry(String name, int maxSize) {
		this.name = name;
		this.maxSize = maxSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
