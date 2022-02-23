package com.github.zubmike.service.demo.utils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.validation.constraints.Null;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteArrayServletInputStream extends ServletInputStream {

	private final ByteArrayInputStream byteArrayInputStream;

	@Null
	private ReadListener readListener;

	public ByteArrayServletInputStream(byte[] bytes) {
		this.byteArrayInputStream = new ByteArrayInputStream(bytes);
	}

	@Override
	public int read() {
		var ch = byteArrayInputStream.read();
		notifyListener();
		return ch;
	}

	@Override
	public boolean isFinished() {
		return byteArrayInputStream.available() == 0;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		this.readListener = readListener;
		notifyListener();
	}

	private void notifyListener() {
		if (readListener == null) {
			return;
		}
		if (!isFinished()) {
			try {
				readListener.onDataAvailable();
			} catch (IOException e) {
				readListener.onError(e);
			}
		} else {
			try {
				readListener.onAllDataRead();
			} catch (IOException e) {
				readListener.onError(e);
			}
		}
	}

}
