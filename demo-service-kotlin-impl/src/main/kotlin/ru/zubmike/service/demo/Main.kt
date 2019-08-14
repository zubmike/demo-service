package ru.zubmike.service.demo

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.Koin
import ru.zubmike.service.demo.api.dictionaries
import ru.zubmike.service.demo.api.starships
import ru.zubmike.service.demo.api.zones
import ru.zubmike.service.demo.conf.handleException
import ru.zubmike.service.demo.conf.koinModule
import ru.zubmike.service.demo.conf.logRequest

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging) {
        logRequest()
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(Koin) {
        modules(koinModule)
    }
    install(StatusPages) {
        handleException()
    }
    install(Routing) {
        trace { application.log.info(it.buildText()) }
        dictionaries()
        starships()
        zones()
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, module = Application::module).start()
}
