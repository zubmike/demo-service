package com.github.zubmike.service.demo.conf

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.github.zubmike.core.utils.DataSourceException
import com.github.zubmike.core.utils.InvalidParameterException
import com.github.zubmike.core.utils.NotFoundException
import com.github.zubmike.service.utils.AuthException
import com.github.zubmike.service.utils.DuplicateException

object ExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(ExceptionHandler::class.java)

}

fun StatusPages.Configuration.handleException() {
    exception<DuplicateException> {
        call.respond(HttpStatusCode.BadRequest, it.message?: "Duplicate")
    }
    exception<InvalidParameterException> {
        call.respond(HttpStatusCode.BadRequest, it.message?: "Invalid parameter")
    }
    exception<AuthException> {
        call.respond(HttpStatusCode.Unauthorized, it.message?: "Unauthorized")
    }
    exception<NotFoundException> {
        call.respond(HttpStatusCode.NotFound, it.message?:"Not found")
    }
    exception<DataSourceException> {
        ExceptionHandler.logger.error(it.message, it)
        call.respond(HttpStatusCode.InternalServerError, "Data source problem")
    }
    exception<Throwable> {
        ExceptionHandler.logger.error(it.message, it)
        call.respond(HttpStatusCode.InternalServerError, "Internal server error")
    }
}