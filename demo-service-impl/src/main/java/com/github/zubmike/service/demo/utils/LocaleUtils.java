package com.github.zubmike.service.demo.utils;

import com.github.zubmike.core.utils.CollectionUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LocaleUtils {

	public static Locale getLocale(HttpHeaders headers) {
		return getLocale(headers.getAcceptableLanguages());
	}

	public static Locale getLocale(ContainerRequestContext requestContext) {
		return getLocale(requestContext.getAcceptableLanguages());
	}

	public static Locale getLocale(List<Locale> requestLocales) {
		if (CollectionUtils.isNotEmpty(requestLocales)) {
			var requestLocale = requestLocales.get(0);
			if (!Objects.equals(requestLocale.getLanguage(), "*")) {
				return requestLocale;
			}
		}
		return Locale.getDefault();
	}

}
