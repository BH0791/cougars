package fr.hamtec.plugins

import fr.hamtec.FileHeadlog.logger
import fr.hamtec.data.Team
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

/**
/ 2- Il est la deuxième étape sur la route de la requête
 */
fun Application.configureRequestValidation() {
    install(RequestValidation) {
        logger.info("+++++ Entrée +++++")
        validate<Team> { team ->
            if(team.id <= 0) {
                logger.info("+++++ Invalide +++++")
                ValidationResult.Invalid("L'identifiant de l'équipe ne doit pas être égal à zéro ou à une valeur négative...$team")
            } else {
                logger.info("+++++ Valide +++++")
                ValidationResult.Valid
            }
        }
    }
}