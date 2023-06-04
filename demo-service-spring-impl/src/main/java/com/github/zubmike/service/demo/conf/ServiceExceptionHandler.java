package com.github.zubmike.service.demo.conf;

import com.github.zubmike.core.utils.NotFoundException;
import com.github.zubmike.service.demo.ServiceResource;
import com.github.zubmike.service.demo.utils.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ServiceExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

	private static final String RESPONSE_CONTENT_TYPE = MediaType.TEXT_PLAIN + ";charset=utf-8";

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResponseEntity<?> handle(Exception exception, HttpServletRequest request) {
		var message = exception.getMessage();
		var locale = request.getLocale();
		if (exception instanceof AuthException) {
			return createResponse(HttpStatus.UNAUTHORIZED).body(message);
		} else if (exception instanceof IllegalArgumentException) {
			return createResponse(HttpStatus.BAD_REQUEST).body(message);
		} else if (exception instanceof NotFoundException
				|| exception instanceof MethodArgumentTypeMismatchException
				|| exception instanceof MethodArgumentNotValidException) {
			return createResponse(HttpStatus.NOT_FOUND).body(
					exception.getMessage() == null ? ServiceResource.getString(locale, "res.string.notFound") : exception.getMessage());
		} else if (exception instanceof HttpRequestMethodNotSupportedException) {
			return createResponse(HttpStatus.METHOD_NOT_ALLOWED).body(message);
		} else if (exception instanceof HttpMediaTypeNotSupportedException) {
			return createResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ServiceResource.getString(locale, "res.string.unsupportedDataType"));
		} else {
			LOGGER.error(message, exception);
			return createResponse(HttpStatus.INTERNAL_SERVER_ERROR).body(ServiceResource.getString(locale, "res.string.internalServerError"));
		}
	}

	private ResponseEntity.BodyBuilder createResponse(HttpStatus status) {
		return ResponseEntity.status(status).header(HttpHeaders.CONTENT_TYPE, RESPONSE_CONTENT_TYPE);
	}
}
