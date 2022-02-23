package com.github.zubmike.service.demo.dao.db;

import com.github.zubmike.service.dao.db.BasicEntityItemDao;
import com.github.zubmike.service.demo.dao.UserDao;
import com.github.zubmike.service.demo.types.User;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.Optional;

public class UserDaoImpl extends BasicEntityItemDao<Integer, User> implements UserDao {

	@Inject
	public UserDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, User.class);
	}

	@Override
	public Optional<User> findByLogin(String login) {
		return runAndReturn(session -> session.createQuery("from User where login = :login", User.class)
				.setParameter("login", login)
				.uniqueResultOptional());
	}
}
