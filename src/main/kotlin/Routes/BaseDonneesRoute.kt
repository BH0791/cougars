package fr.hamtec.Routes

import fr.hamtec.data.Teams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
//import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configDBRoute() {
    routing {
        get("/aff1") {
            val teams = transaction {
                Teams.selectAll().map { it[Teams.name] }
            }
            call.respond(teams) // Retourne la liste directement.
        }
        get("/affiche") {
            println("********** Routes...")
            val teams = transaction {
                Teams.selectAll().mapNotNull { it[Teams.name] } // Ignore les nulls, si pertinents.
            }
            println(teams)
            if(teams.isEmpty()) {
                call.respondText("Aucune équipe trouvée.", status = HttpStatusCode.NoContent)
            } else {
                call.respond(teams) // Retourne la liste, même vide.
            }
        }
        get("/aff") {
            val team = transaction {
                Teams.selectAll()
                    .map { it[Teams.name] }
                    .firstOrNull()
            }

            if(team != null) {
                call.respond(team) // Retourne le nom de la première équipe.
            } else {
                call.respond(HttpStatusCode.NotFound, "Aucune équipe trouvée.")
            }
        }
        get("/aff/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if(id != null) {
                val team = transaction {
                    exec("SELECT name FROM teams WHERE id = $id") {
                        if(it.next()) it.getString("name") else null
                    }
                }

                if(team != null) {
                    call.respond(team)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Équipe non trouvée.")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "ID invalide.")
            }
        }
        get("/good") {
//*marche pas
            val teamName: String? = transaction {
                Teams.select(Teams.id eq 0)
                    .map { it[Teams.name] } // Récupère uniquement le champ `name`
                    .singleOrNull()         // Retourne un seul résultat ou null
            }
            call.respond("$teamName")
        }
        get("/insert") {
            transaction {
                val id = Teams.insertAndGetId {
                    it[name] = "Laos"
                    it[firstName] = "laotien"
                }
            }
            call.respond("Insertion réusit")
        }
    }
}