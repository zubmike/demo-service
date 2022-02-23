package com.github.zubmike.service.demo.api;

import com.github.zubmike.core.utils.DataSourceException;
import com.github.zubmike.core.utils.DuplicateException;
import com.github.zubmike.core.utils.InvalidParameterException;
import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.utils.LocaleUtils;
import com.github.zubmike.service.utils.AuthException;
import com.google.common.base.Strings;
import org.glassfish.jersey.message.internal.HeaderValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperImpl implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMapperImpl.class);

	private static final String RESPONSE_CONTENT_TYPE = MediaType.TEXT_PLAIN + ";charset=utf-8";

	@Context
	private HttpHeaders headers;

	@Override
	public Response toResponse(Exception exception) {
		var message = exception.getMessage();
		var locale = LocaleUtils.getLocale(headers);
		if (exception instanceof DuplicateException) {
			return createResponse(Response.Status.BAD_REQUEST, message);
		} else if (exception instanceof InvalidParameterException) {
			return createResponse(Response.Status.BAD_REQUEST, message);
		} else if (exception instanceof IllegalArgumentException) {
			return createResponse(Response.Status.BAD_REQUEST, message);
		} else if (exception instanceof HeaderValueException) {
			return createResponse(Response.Status.BAD_REQUEST, ServiceResource.getString(locale, "res.string.invalidParameters"));
		} else if (exception instanceof AuthException) {
			return createResponse(Response.Status.UNAUTHORIZED, message);
		} else if (exception instanceof NotFoundException) {
			return createResponse(Response.Status.NOT_FOUND,
					!Strings.isNullOrEmpty(message) ? message : ServiceResource.getString(locale, "res.string.notFound"));
		} else if (exception instanceof javax.ws.rs.NotFoundException) {
			return createResponse(Response.Status.NOT_FOUND, ServiceResource.getString(locale, "res.string.apiNotFound"));
		} else if (exception instanceof NotAllowedException) {
			return createResponse(Response.Status.METHOD_NOT_ALLOWED, null);
		} else if (exception instanceof NotSupportedException) {
			return createResponse(Response.Status.UNSUPPORTED_MEDIA_TYPE, ServiceResource.getString(locale, "res.string.unsupportedDataType"));
		} else if (exception instanceof DataSourceException) {
			LOGGER.error(message, exception);
			return createResponse(Response.Status.INTERNAL_SERVER_ERROR, ServiceResource.getString(locale, "res.string.dataSourceProblem"));
		} else {
			LOGGER.error(message, exception);
			return createResponse(Response.Status.INTERNAL_SERVER_ERROR, ServiceResource.getString(locale, "res.string.internalServerError"));
		}
	}

	private static Response createResponse(Response.Status status, String message) {
		return Response
				.status(status.getStatusCode())
				.type(RESPONSE_CONTENT_TYPE)
				.entity(message)
				.build();
	}

}
