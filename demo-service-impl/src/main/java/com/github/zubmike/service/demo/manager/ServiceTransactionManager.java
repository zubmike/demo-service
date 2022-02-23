package com.github.zubmike.service.demo.manager;

import com.github.zubmike.service.managers.DbTransactionManager;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class ServiceTransactionManager extends DbTransactionManager {

	@Inject
	public ServiceTransactionManager(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
