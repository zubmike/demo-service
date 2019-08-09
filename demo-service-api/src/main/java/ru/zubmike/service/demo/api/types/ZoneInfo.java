package ru.zubmike.service.demo.api.types;

import ru.zubmike.core.types.DictItem;

public class ZoneInfo extends ZoneEntry implements DictItem<Integer> {

	private static final long serialVersionUID = 1L;

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
