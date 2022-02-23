package com.github.zubmike.service.demo.api.types;

import com.github.zubmike.core.types.DictItem;

import java.io.Serial;

public class ZoneInfo extends ZoneEntry implements DictItem<Integer> {

	@Serial
	private static final long serialVersionUID = -5687033809529363540L;

	private int id;

	public ZoneInfo() {
	}

	public ZoneInfo(int id, String name, int maxSize) {
		super(name, maxSize);
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
