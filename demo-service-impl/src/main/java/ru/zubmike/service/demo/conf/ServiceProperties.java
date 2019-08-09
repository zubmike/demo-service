package ru.zubmike.service.demo.conf;

import ru.zubmike.service.conf.DataBaseProperties;

import java.io.Serializable;

public class ServiceProperties implements Serializable {

	private static final long serialVersionUID = 2L;

	private DataBaseProperties dataBase;

	public DataBaseProperties getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBaseProperties dataBase) {
		this.dataBase = dataBase;
	}
}
