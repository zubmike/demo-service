package com.github.zubmike.service.demo.conf;

import com.google.common.base.Strings;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.zubmike.core.utils.DataSourceException;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.utils.AuthException;
import com.github.zubmike.service.utils.DuplicateException;

import javax.inject.Inject;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ExceptionMapperImpl implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMapperImpl.class);

	@Inject
	public ExceptionMapperImpl(ResourceConfig resourceConfig) {
		resourceConfig.register(this);
	}

	@Override
	public Response toResponse(Exception exception) {
		String message = exception.getMessage();
		if (exception instanceof DuplicateException) {
			return createResponse(Response.Status.BAD_REQUEST, message);
		} else if (exception instanceof InvalidParameterException) {
			return createResponse(Response.Status.BAD_REQUEST, message);
		} else if (exception instanceof IllegalArgumentException) {
			return createResponse(Response.Status.BAD_REQUEST, message);
		} else if (exception instanceof AuthException) {
			return createResponse(Response.Status.UNAUTHORIZED, message);
		} else if (exception instanceof NotFoundException) {
			return createResponse(Response.Status.NOT_FOUND, Strings.isNullOrEmpty(exception.getMessage()) ? "Not found" : exception.getMessage());
		} else if (exception instanceof javax.ws.rs.NotFoundException) {
			return createResponse(Response.Status.NOT_FOUND, null);
		} else if (exception instanceof NotAllowedException) {
			return createResponse(Response.Status.METHOD_NOT_ALLOWED, null);
		} else if (exception instanceof NotSupportedException) {
			return createResponse(Response.Status.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type");
		} else if (exception instanceof DataSourceException) {
			LOGGER.error(message, exception);
			return createResponse(Response.Status.INTERNAL_SERVER_ERROR, "Data source problem");
		} else {
			LOGGER.error(message, exception);
			return createResponse(Response.Status.INTERNAL_SERVER_ERROR, "Internal server error");
		}
	}

	private static Response createResponse(Response.Status status, String message) {
		return Response.status(status.getStatusCode()).entity(message).type(MediaType.TEXT_PLAIN).build();
	}

}
