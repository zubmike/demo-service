package com.github.zubmike.service.demo;

import com.github.zubmike.service.demo.types.ServiceUserContext;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ServiceResource {

	private static final String STRING_BUNDLE = "bundles/string";

	public static String getString(Locale locale, String key, Object... args) {
		try {
			var text = ResourceBundle.getBundle(STRING_BUNDLE, locale).getString(key);
			return args != null ? String.format(text, args) : text;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public static String getString(ServiceUserContext serviceUserContext, String key, Object... args) {
		return getString(serviceUserContext.getLocale(), key, args);
	}

	public static String getString(String key, Object... args) {
		return getString(Locale.getDefault(), key, args);
	}

}
