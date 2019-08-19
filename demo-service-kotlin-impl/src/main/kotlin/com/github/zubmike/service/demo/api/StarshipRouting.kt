package com.github.zubmike.service.demo.api

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import com.github.zubmike.service.demo.logic.StarshipLogic

fun Routing.starships() {

    route("/starships") {

        val starshipLogic: StarshipLogic by inject()

        post {
            call.respond(starshipLogic.addStarship(call.receive()))
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toLong()
            call.respond(starshipLogic.getStarship(id))
        }

        get("/number/{number}") {
            val number = call.parameters["number"]!!.toString()
            call.respond(starshipLogic.getStarship(number))
        }

    }

}