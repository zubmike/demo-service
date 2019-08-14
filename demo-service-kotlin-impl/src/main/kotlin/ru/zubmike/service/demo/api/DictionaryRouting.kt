package ru.zubmike.service.demo.api

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import ru.zubmike.service.demo.logic.DictionaryLogic

fun Routing.dictionaries() {

    route("/dictionaries") {

        val dictionaryLogic: DictionaryLogic by inject()

        get("/planetary-systems") {
            call.respond(dictionaryLogic.getPlanetarySystems())
        }
    }

}