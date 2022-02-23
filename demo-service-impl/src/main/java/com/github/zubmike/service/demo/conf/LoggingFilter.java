package com.github.zubmike.service.demo.conf;

import com.github.zubmike.service.demo.utils.HttpServletRequestContentWrapper;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class LoggingFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
		long startTimeMillis = System.currentTimeMillis();
		var request = new HttpServletRequestContentWrapper((HttpServletRequest) servletRequest);
		var response = (HttpServletResponse) servletResponse;
		chain.doFilter(request, response);
		logRequest(request, response, startTimeMillis);
	}

	@Override
	public void destroy() {

	}

	private static void logRequest(String address, String requestInfo, String responseInfo) {
		LOGGER.info("{} -> {}\n <- {}", address, requestInfo, responseInfo);
	}

	private void logRequest(HttpServletRequestContentWrapper request, HttpServletResponse response, long startTimeMillis) {
		logRequest(request.getRemoteAddr(),
				createRequestInfo(request),
				createResponseInfo(startTimeMillis, response));
	}

	public static void logRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		logRequest(servletRequest.getRemoteAddr(),
				createRequestInfo(servletRequest) + "\n",
				String.valueOf(servletResponse.getStatus()));
	}

	private String createRequestInfo(HttpServletRequestContentWrapper request) {
		var queryString = request.getQueryString();
		return request.getMethod() + " " + request.getRequestURL() + (queryString != null ? "?" + queryString : "" ) +
				"\n\t" + getHeaderInfo(request) +
				getRequestBody(request);
	}

	private static String createRequestInfo(HttpServletRequest servletRequest) {
		var queryString = servletRequest.getQueryString();
		return servletRequest.getMethod() + " " + servletRequest.getRequestURL() + (queryString != null ? "?" + queryString : "" ) +
				"\n\t" + getHeaderInfo(servletRequest);
	}

	private static String getHeaderInfo(HttpServletRequest servletRequest) {
		var headers = new ArrayList<String>();
		var headerNames = servletRequest.getHeaderNames();
		if (headerNames != null) {
			headerNames.asIterator().forEachRemaining(headerName -> {
				headers.add(headerName + ": " + servletRequest.getHeader(headerName));
			});
		}
		return String.join("\n\t", headers);
	}

	private String getRequestBody(HttpServletRequestContentWrapper request) {
		var stringBuilder = new StringBuilder("\n");
		if (request.isWrapped()) {
			byte[] bytes = request.getContentBytes();
			if (bytes != null) {
				var body = new String(bytes, Charsets.UTF_8)
						.replaceAll("\"password\"\\s*:\\s*(\"(?:\\\\\"|[^\"])*?\")", "\"password\":***");
				stringBuilder.append("\t").append(body);
			}
		}
		return stringBuilder.toString();
	}

	private String createResponseInfo(long requestStartMillis, HttpServletResponse responseContext) {
		var stateCode = String.valueOf(responseContext.getStatus());
		long requestTime = System.currentTimeMillis() - requestStartMillis;
		return stateCode + " (" + requestTime + "ms)";
	}

}
