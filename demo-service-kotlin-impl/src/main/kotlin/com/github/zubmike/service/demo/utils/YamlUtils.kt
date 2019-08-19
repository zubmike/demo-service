package com.github.zubmike.service.demo.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.zubmike.core.utils.InternalException

import java.io.File

object YamlUtils {

    private val OBJECT_MAPPER = ObjectMapper(YAMLFactory())

    fun <T> parse(configurationFile: String, clazz: Class<T>): T {
        try {
            return OBJECT_MAPPER.readValue(File(configurationFile), clazz)
        } catch (e: Exception) {
            throw InternalException(e)
        }

    }

}
