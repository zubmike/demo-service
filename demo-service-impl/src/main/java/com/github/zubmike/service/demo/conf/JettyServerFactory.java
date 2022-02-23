package com.github.zubmike.service.demo.conf;

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.EnumSet;

public class JettyServerFactory {

	public static Server create(ServiceProperties serviceProperties) {
		var servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath(serviceProperties.getServer().getContextUrl());
		servletContextHandler.setAttribute(ServiceProperties.class.getSimpleName(), serviceProperties);
		servletContextHandler.addEventListener(new ServiceGuiceServletContextListener(serviceProperties));

		servletContextHandler.addFilter(LoggingFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST, DispatcherType.INCLUDE));

		var servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitOrder(0);
		servletHolder.setInitParameter("javax.ws.rs.Application", ServiceResourceConfig.class.getName());

		var server = new Server(serviceProperties.getServer().getPort());
		server.setHandler(servletContextHandler);
		server.setErrorHandler(new JettyErrorHandler());
		server.setStopAtShutdown(true);

		return server;
	}

	private static class JettyErrorHandler extends ErrorHandler {

		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
			int statusCode = response.getStatus();
			if (statusCode != HttpServletResponse.SC_BAD_REQUEST &&
					statusCode != HttpServletResponse.SC_UNAUTHORIZED &&
					statusCode != HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}

	}

}
