package fr.hamtec.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import fr.hamtec.jwtSecret
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

//-Authentification Basic
//fun Application.configureAuthentication(){
//    install(Authentication){
//        basic("auth-basic") {
//            realm = "Ktor Server"
//            validate { credentials ->
//                if (credentials.name == "admin" && credentials.password == "admin") {
//                    UserIdPrincipal(credentials.name)
//                } else {
//                    null
//                }
//            }
//        }
//    }
//}
//-Utilisation d'un token jwt
fun Application.configureAuthentication(){
    install(Authentication){
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .build()
            )
            validate { credential ->
                val role = credential.payload.getClaim("role").asString() // Récupère le claim "role"
                println("Role extrait du token : $role Log pour déboguer") // Log pour déboguer

                if (role != null && role == "admin") {
                    JWTPrincipal(credential.payload) // Valide si le rôle est "admin"
                } else {
                    println("Validation échouée : rôle absent ou non valide.")
                    null // Retourne null si le rôle est incorrect
                }
            }
        }

    }
}