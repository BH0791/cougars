package fr.hamtec.plugins


//import fr.hamtec.FileHeadlog.logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, "Erreur de validation : ${cause.reasons.joinToString(", ")}")
        }
    }
}
//fun Application.configureStatusPages() {
//    install(StatusPages) {
//        exception<RequestValidationException> { call, cause ->
//            // Journalisation des erreurs
//            cause.reasons.forEach { reason ->
//                logger.info("********** Erreur de validation détectée : $reason")
//            }
//
//            // Réponse HTTP au client
//            call.respond(HttpStatusCode.BadRequest, "Erreurs de validation : ${cause.reasons.joinToString(", ")}")
//        }
//    }
//}

