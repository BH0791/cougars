package fr.hamtec.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

/**
 / 1- Il est la première étape sur la route de la requête
 */
fun Application.configureContentNegotiation() {
    install(ContentNegotiation){
        json(
            Json {
                prettyPrint = true
                isLenient = true
                encodeDefaults = true
            }
        )
    }
}