package fr.hamtec.Routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import fr.hamtec.FileHeadlog.logHeaders
import fr.hamtec.FileHeadlog.logResponseHeaders
import fr.hamtec.data.Team
import fr.hamtec.data.UserCredentials
import fr.hamtec.genToken
import fr.hamtec.jwtSecret
import fr.hamtec.utils.authenticateUser
import io.ktor.http.*
import io.ktor.http.HttpHeaders.Date
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title
import java.io.File
import java.util.Date


fun Application.configureTeamsRouting() {
    routing {
        staticFiles("/static", File("my_static_files_dir"))
        staticResources("/website", "frontend")
        get("/teams") {
            logHeaders(call)
            call.respondText("Hello World!")
            logResponseHeaders(call)
        }
        get("/teams/") {
            logHeaders(call)
            val country: String? = call.queryParameters["country"]
            if(country == null) {
                call.respond(HttpStatusCode.BadRequest, "xxxxx")
            } else {
                call.respond(HttpStatusCode.OK, "Good!")
            }
            logResponseHeaders(call)
        }
        post("/teams") {
            logHeaders(call)
            val team = call.receive<Team>()
            call.response.header("X-man", "hamtec")
            call.respond(HttpStatusCode.OK, team.id)
            logResponseHeaders(call)
        }
//        authenticate("auth-basic") {
//
//            get("/fichier") {
//                logHeaders(call)
//                val userInfo = call.principal<UserIdPrincipal>()
//
//                val name = "Ktor"
//                call.respondHtml(HttpStatusCode.OK) {
//                    head {
//                        attributes["class"] = "tete"
//                        title {
//                            + "${userInfo?.name}"
//                        }
//                    }
//                    body {
//                        h1 {
//                            +"Hello ${userInfo?.name} from $name!"
//                        }
//                    }
//                }
//                logResponseHeaders(call)
//            }
//        }

        post("/login") {
            val userCredentials = call.receive<UserCredentials>()
            if(userCredentials.username == "kotlin" && userCredentials.password == "boock"){
                val token = JWT.create()
                    .withClaim("username", userCredentials.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60_000))
                    .sign(Algorithm.HMAC256(jwtSecret))
                call.respond(HttpStatusCode.OK, hashMapOf("token" to token))
            }else{
                call.respond(HttpStatusCode.Unauthorized)
            }
        }



        authenticate {
            get("/hello") {
            logHeaders(call)

                val jwtPrincipal = call.principal<JWTPrincipal>()
                call.respond(HttpStatusCode.OK, "Hello ${jwtPrincipal!!.payload.getClaim("username")}")
            }
        }
    }
}