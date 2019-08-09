package ru.zubmike.service.demo.conf;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class.getPackage().getName());
	private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

	private static final String REQUEST_ID_HEADER = "X-Request-ID";
	@Context
	private HttpServletRequest httpServletRequest;

	private final ConcurrentMap<String, byte[]> wrappedRequestBodyMap = Maps.newConcurrentMap();
	private final ConcurrentMap<String, Long> requestStartTimeMap = Maps.newConcurrentMap();

	@Inject
	public LoggingFilter(ResourceConfig resourceConfig) {
		resourceConfig.register(this);
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {
		long startTimeMillis = System.currentTimeMillis();
		String requestId = UUID.randomUUID().toString();
		requestContext.getHeaders().putSingle(REQUEST_ID_HEADER, requestId);
		try {
			requestStartTimeMap.put(requestId, startTimeMillis);
			if (isWrappedRequest(requestContext)) {
				wrapRequestBody(requestId, requestContext);
			}
		} catch (Exception e) {
			wrappedRequestBodyMap.remove(requestId);
			requestStartTimeMap.remove(requestId);
		}
	}

	private boolean isWrappedRequest(ContainerRequestContext requestContext) {
		return requestContext.getLength() > 0;
	}

	private void wrapRequestBody(String requestId, ContainerRequestContext requestContext) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = requestContext.getEntityStream();
		try {
			ReaderWriter.writeTo(in, out);
			byte[] bytes = out.toByteArray();
			if (bytes.length > 0) {
				wrappedRequestBodyMap.put(requestId, bytes);
			}
			requestContext.setEntityStream(new ByteArrayInputStream(bytes) );
		} catch (Exception e) {
			LOGGER.error("can't get request body", e);
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		String requestId = requestContext.getHeaderString(REQUEST_ID_HEADER);
		try {
			REQUEST_LOGGER.info("{} -> {}\n <- {}",
					httpServletRequest.getRemoteAddr(),
					createRequestInfo(requestContext),
					createResponseInfo(requestContext,  responseContext));
		} finally {
			if (requestId != null) {
				wrappedRequestBodyMap.remove(requestId);
				requestStartTimeMap.remove(requestId);
			}
		}
	}

	private String createRequestInfo(ContainerRequestContext requestContext) {
		return requestContext.getMethod() + " " + requestContext.getUriInfo().getRequestUri() +
				"\n\t" + getHeaderInfo(requestContext) +
				getRequestBody(requestContext);
	}

	private static String getHeaderInfo(ContainerRequestContext requestContext) {
		List<String> headers = Lists.newArrayList();
		for (String headerKey : requestContext.getHeaders().keySet()) {
			for (String headerValue : requestContext.getHeaders().get(headerKey)) {
				headers.add(headerKey + ": " + headerValue);
			}
		}
		return String.join("\n\t", headers);
	}

	private String getRequestBody(ContainerRequestContext requestContext) {
		StringBuilder builder = new StringBuilder();
		if (isWrappedRequest(requestContext)) {
			byte[] bytes = getRequestBytes(requestContext);
			if (bytes != null) {
				builder.append("\n\n\t").append(new String(bytes, Charsets.UTF_8));
			}
		}
		return builder.toString();
	}

	private byte[] getRequestBytes(ContainerRequestContext requestContext) {
		String requestId = requestContext.getHeaderString(REQUEST_ID_HEADER);
		return Strings.isNullOrEmpty(requestId) ? new byte[0] : wrappedRequestBodyMap.get(requestId);
	}

	private String createResponseInfo(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		Long startRequestTime = getStartRequestTime(requestContext);
		String stateCode = String.valueOf(responseContext.getStatus());
		if (startRequestTime == null) {
			return stateCode;
		} else {
			long requestTime = System.currentTimeMillis() - startRequestTime;
			return stateCode + " (" + requestTime + "ms)";
		}
	}

	private Long getStartRequestTime(ContainerRequestContext requestContext) {
		String requestId = requestContext.getHeaderString(REQUEST_ID_HEADER);
		return Strings.isNullOrEmpty(requestId) ? null : requestStartTimeMap.get(requestId);
	}
}
