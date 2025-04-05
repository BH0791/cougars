package fr.hamtec

import fr.hamtec.Routes.configDBRoute
import fr.hamtec.Routes.configureCRUD
import io.ktor.server.application.*

fun Application.configureRouting() {
    //configDBRoute()
    configureCRUD()
}
