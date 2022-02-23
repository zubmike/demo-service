package com.github.zubmike.service.demo.manager;

import com.github.zubmike.service.conf.FileKeyStoreProperties;
import com.github.zubmike.service.managers.FileKeyStoreManager;

import javax.inject.Inject;

public class ServiceKeyStoreManager extends FileKeyStoreManager {

	@Inject
	public ServiceKeyStoreManager(FileKeyStoreProperties fileKeyStoreProperties) {
		super(fileKeyStoreProperties);
	}

}
