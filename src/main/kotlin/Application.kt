package fr.hamtec

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import fr.hamtec.plugins.*
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*
import io.ktor.server.cio.*

val dotenv = Dotenv.load()
val jwtSecret: String = dotenv["JWT_SECRET"] ?: "defaut_secret"


fun main(args: Array<String>) = EngineMain.main(args)


fun Application.module() {
    configureContentNegotiation()
    configureRequestValidation()
    configureStatusPages()
    configureAuthentication()
    configureWebSockets()

    genToken()

    configureRouting()
}

fun genToken(){
    val token = JWT.create()
        .withClaim("role", "admin")
        .sign(Algorithm.HMAC256("hamtec57"))
    println(token)
}


