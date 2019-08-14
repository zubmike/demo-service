package ru.zubmike.service.demo.conf

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.zubmike.core.utils.DataSourceException
import ru.zubmike.core.utils.InvalidParameterException
import ru.zubmike.core.utils.NotFoundException
import ru.zubmike.service.utils.AuthException
import ru.zubmike.service.utils.DuplicateException

object ExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(ExceptionHandler::class.java)

}

fun StatusPages.Configuration.handleException() {
    exception<Throwable> { cause ->
        when (cause) {
            is DuplicateException -> call.respond(HttpStatusCode.BadRequest, cause.message?: "Duplicate")
            is InvalidParameterException -> call.respond(HttpStatusCode.BadRequest, cause.message?: "Invalid parameter")
            is AuthException -> call.respond(HttpStatusCode.Unauthorized, cause.message?: "Unauthorized")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, cause.message?:"Not found")
            is DataSourceException -> {
                ExceptionHandler.logger.error(cause.message, cause)
                call.respond(HttpStatusCode.InternalServerError, "Data source problem")
            }
            else -> {
                ExceptionHandler.logger.error(cause.message, cause)
                call.respond(HttpStatusCode.InternalServerError, "Internal server error")
            }
        }
        throw cause
    }
}