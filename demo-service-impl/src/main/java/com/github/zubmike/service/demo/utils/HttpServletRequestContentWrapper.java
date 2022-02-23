package com.github.zubmike.service.demo.utils;

import com.github.zubmike.service.utils.EntityContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class HttpServletRequestContentWrapper extends HttpServletRequestWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletRequestContentWrapper.class);

	private static final int MAX_WRAP_CONTENT_BYTE_SIZE = 512;

	private final HttpServletRequest request;

	private final boolean wrapped;

	private byte[] contentBytes;

	public HttpServletRequestContentWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
		if (isNeededWrap()) {
			copyContent();
			this.wrapped = true;
		} else {
			this.wrapped = false;
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (isWrapped()) {
			return new ByteArrayServletInputStream(contentBytes);
		} else {
			return super.getInputStream();
		}
	}

	private void copyContent() {
		try (ServletInputStream inputStream = super.getInputStream()) {
			contentBytes = inputStream.readAllBytes();
		} catch (Exception e) {
			LOGGER.error("can't wrap request content", e);
		}
	}

	private boolean isNeededWrap() {
		return request.getContentLength() > 0
				&& request.getContentLength() <= MAX_WRAP_CONTENT_BYTE_SIZE
				&& request.getContentType() != null
				&& !request.getContentType().equals(MediaType.MULTIPART_FORM_DATA);
	}

	public boolean isWrapped() {
		return wrapped;
	}

	public byte[] getContentBytes() {
		return contentBytes;
	}
}
