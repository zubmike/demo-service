package com.github.zubmike.service.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.zubmike.core.utils.InternalException;

import java.io.File;

public class YamlUtils {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

	public static <T> T parse(String configurationFile, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(new File(configurationFile), clazz);
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

}
