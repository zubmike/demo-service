package com.github.zubmike.service.demo.dao;

import com.github.zubmike.service.dao.EntityItemDao;
import com.github.zubmike.service.demo.types.User;

import java.util.Optional;

public interface UserDao extends EntityItemDao<Integer, User> {

	Optional<User> findByLogin(String login);

}
