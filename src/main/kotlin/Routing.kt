package fr.hamtec

import fr.hamtec.Routes.configDBRoute
import io.ktor.server.application.*

fun Application.configureRouting() {
    configDBRoute()
}
