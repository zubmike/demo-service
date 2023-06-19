package com.github.zubmike.service.demo.conf;

import com.github.zubmike.service.demo.services.AuthService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

	@Autowired
	private AuthService authService;

	@Autowired
	private ServiceExceptionHandler serviceExceptionHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
									FilterChain filterChain) throws ServletException, IOException {
		var authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if (!Strings.isEmpty(authHeader)) {
			try {
				authService.getServiceUserContext(httpServletRequest.getLocale(), authHeader)
						.ifPresent(serviceUserContext ->
								SecurityContextHolder.getContext().setAuthentication(serviceUserContext));
			} catch (Exception e) {
				var exeptionResponseEntity = serviceExceptionHandler.handle(e, httpServletRequest);
				replaceResponse(httpServletResponse, exeptionResponseEntity);
				return;
			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private static void replaceResponse(HttpServletResponse httpServletResponse, ResponseEntity<String> exeptionResponseEntity) throws IOException {
		exeptionResponseEntity.getHeaders().forEach((key, values) ->
				values.forEach(value -> httpServletResponse.addHeader(key, value)));
		httpServletResponse.setStatus(exeptionResponseEntity.getStatusCodeValue());
		if (exeptionResponseEntity.getBody() != null) {
			httpServletResponse.getWriter().write(exeptionResponseEntity.getBody());
		}
	}

}
