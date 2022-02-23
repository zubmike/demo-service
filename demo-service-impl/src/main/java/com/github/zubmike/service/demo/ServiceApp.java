package com.github.zubmike.service.demo;

import com.github.zubmike.service.demo.conf.JettyServerFactory;
import com.github.zubmike.service.demo.conf.ServiceProperties;
import com.github.zubmike.service.demo.utils.YamlUtils;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class ServiceApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceApp.class);

	private static final String DEFAULT_CONFIGURATION_PATH = "config.yml";

	public static void main(String[] args) {
		Server server = null;
		try {
			LOGGER.info("init server");
			Locale.setDefault(Locale.UK);
			var serviceProperties = YamlUtils.parse(DEFAULT_CONFIGURATION_PATH, ServiceProperties.class);
			server = JettyServerFactory.create(serviceProperties);
			LOGGER.info("start server");
			server.start();
			server.join();
		} catch (Exception e) {
			LOGGER.error("can't start server", e);
			System.exit(1);
		} finally {
			LOGGER.info("stop server");
			if (server != null) {
				server.destroy();
			}
		}
	}

}
