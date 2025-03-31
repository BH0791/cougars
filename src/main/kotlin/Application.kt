package fr.hamtec

import fr.hamtec.db.configurationDB
import fr.hamtec.plugins.*
import fr.hamtec.utils.genToken
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*
import io.ktor.server.cio.*
import kotlinx.coroutines.launch

//* https://github.com/cdimascio/dotenv-kotlin
val dotenv = Dotenv.load()
val jwtSecret: String = dotenv["JWT_SECRET"] ?: "defaut_secret"


fun main(args: Array<String>) = EngineMain.main(args)


fun Application.module() {
    launch { configurationDB() }
    configureContentNegotiation()
    configureRequestValidation()
    configureStatusPages()
    configureAuthentication()
    configureWebSockets()
//*Affiche le token pour voir les étapes !!! Ne pas faire en production!!!
    genToken()  //********************************************************
//************************************************************************
    configureRouting()
}