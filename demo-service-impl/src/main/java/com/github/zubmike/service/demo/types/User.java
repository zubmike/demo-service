package com.github.zubmike.service.demo.types;

import com.github.zubmike.service.types.BasicEntityDictItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;

@Entity
@Table(name = "users")
public class User extends BasicEntityDictItem {

	@Serial
	private static final long serialVersionUID = 2154873812222200881L;

	@Column(name = "login")
	private String login;

	@Column(name = "password")
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
