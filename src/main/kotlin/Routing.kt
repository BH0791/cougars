package fr.hamtec

import fr.hamtec.Routes.configureRoutesWebSockets
import fr.hamtec.Routes.configureTeamsRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    configureRoutesWebSockets()
}
