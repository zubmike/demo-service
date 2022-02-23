package com.github.zubmike.service.demo.conf;

import com.github.zubmike.service.conf.DataBaseProperties;
import com.github.zubmike.service.conf.FileKeyStoreProperties;
import com.github.zubmike.service.conf.JwtTokenProperties;
import com.github.zubmike.service.conf.ServerProperties;

import java.io.Serial;
import java.io.Serializable;

public class ServiceProperties implements Serializable {

	@Serial
	private static final long serialVersionUID = 5592262469457745082L;

	private ServerProperties server = new ServerProperties();

	private DataBaseProperties dataBase;

	private FileKeyStoreProperties keyStore = new FileKeyStoreProperties();

	private JwtTokenProperties jwtToken = new JwtTokenProperties();

	public ServerProperties getServer() {
		return server;
	}

	public void setServer(ServerProperties server) {
		this.server = server;
	}

	public DataBaseProperties getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBaseProperties dataBase) {
		this.dataBase = dataBase;
	}

	public FileKeyStoreProperties getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(FileKeyStoreProperties keyStore) {
		this.keyStore = keyStore;
	}

	public JwtTokenProperties getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(JwtTokenProperties jwtToken) {
		this.jwtToken = jwtToken;
	}
}
