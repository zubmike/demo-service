package com.github.zubmike.service.demo.conf

import io.ktor.features.CallLogging
import io.ktor.features.origin
import io.ktor.http.Headers
import io.ktor.request.httpMethod
import io.ktor.request.uri
import org.slf4j.event.Level

fun CallLogging.Configuration.logRequest() {
    level = Level.INFO
    format {
        call -> "\n" + call.request.origin.remoteHost +
            " -> " + call.request.httpMethod.value + " " + call.request.origin.host + ":" + call.request.origin.port + call.request.uri +
            "\n\t" + createHeaders(call.request.headers) +
            "\n <- " + call.response.status()?.value
    }
}

private fun createHeaders(values: Headers) : String {
    return values.entries().joinToString(
            separator = "\n\t",
            transform = { it.key + ": " + it.value })
}