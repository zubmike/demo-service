package com.github.zubmike.service.demo.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import com.github.zubmike.service.demo.logic.ZoneLogic

fun Routing.zones() {

    route("/zones") {

        val zoneLogic: ZoneLogic by inject()

        post {
            call.respond(zoneLogic.addZone(call.receive()))
        }

        get {
            call.respond(zoneLogic.getZones())
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            call.respond(zoneLogic.getZone(id))
        }

        post("/{id}/starships/{starship-id}") {
            val id = call.parameters["id"]!!.toInt()
            val starshipId = call.parameters["starship-id"]!!.toLong()
            zoneLogic.addToZone(id, starshipId)
            call.respond(HttpStatusCode.OK)
        }

        delete("/{id}/starships/{starship-id}") {
            val id = call.parameters["id"]!!.toInt()
            val starshipId = call.parameters["starship-id"]!!.toLong()
            zoneLogic.deleteFromZone(id, starshipId)
            call.respond(HttpStatusCode.OK)
        }

        get("/{id}/starships") {
            val id = call.parameters["id"]!!.toInt()
            call.respond(zoneLogic.getZoneStarships(id))
        }
    }

}