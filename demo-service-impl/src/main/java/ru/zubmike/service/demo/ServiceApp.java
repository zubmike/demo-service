package ru.zubmike.service.demo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import ru.zubmike.service.demo.conf.ServiceResourceConfig;

public class ServiceApp {

	private static final int DEFAULT_PORT = 8080;

	public static void main(String[] args) {
		Server server = createJettyServer();
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			System.exit(1);
		} finally {
			server.destroy();
		}
	}

	private static Server createJettyServer() {
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

		servletContextHandler.setContextPath("/");

		ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitOrder(0);
		servletHolder.setInitParameter("javax.ws.rs.Application", ServiceResourceConfig.class.getName());

		Server server = new Server(DEFAULT_PORT);
		server.setHandler(servletContextHandler);

		return server;
	}

}
