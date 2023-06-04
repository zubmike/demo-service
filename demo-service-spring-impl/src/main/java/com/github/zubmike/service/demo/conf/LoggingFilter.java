package com.github.zubmike.service.demo.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebFilter("/*")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		try {
			filterChain.doFilter(request, response);
		} finally {
			LOGGER.info("{} -> {}\n <- {}",
					request.getRemoteAddr(),
					createRequestInfo(wrappedRequest),
					createResponseInfo(response, startTime));
		}
	}

	private String createRequestInfo(HttpServletRequest request) {
		return request.getMethod()
				+ " " + request.getRequestURI()
				+ "\n\t" + getHeaderInfo(request) +
				getRequestBody(request);
	}

	private static String getHeaderInfo(HttpServletRequest request) {
		List<String> headers = new ArrayList<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);
			while (headerValues.hasMoreElements()) {
				headers.add(headerName + ": " + (headerValues.nextElement()));
			}
		}
		return String.join("\n\t", headers);
	}

	private static String getRequestBody(HttpServletRequest request) {
		return new String(((ContentCachingRequestWrapper) request).getContentAsByteArray(), StandardCharsets.UTF_8);
	}

	private static String createResponseInfo(HttpServletResponse response, long startTime) {
		return response.getStatus() + " (" + (System.currentTimeMillis() - startTime) + "ms)";
	}

}
