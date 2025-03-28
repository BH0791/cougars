package fr.hamtec.plugins

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.minutes


/**
 * +Option de ce pluging qui permet de configurer les différents paramètres du protocole WebSoket.
 */
fun Application.configureWebSockets(){
    install(WebSockets){
        pingPeriod = 50.seconds
        timeout = 1.minutes
        masking = false
    }
}