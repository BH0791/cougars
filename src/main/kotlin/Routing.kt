package fr.hamtec

import fr.hamtec.Routes.configureHtmlDslRoute
import io.ktor.server.application.*

fun Application.configureRouting() {
    //configDBRoute()
    //configureCRUD()
    configureHtmlDslRoute()
}
