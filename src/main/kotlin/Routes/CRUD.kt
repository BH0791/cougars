package fr.hamtec.Routes

import fr.hamtec.data.Players
import fr.hamtec.data.Teams
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureCRUD() {
    routing {
        get("/resultRow") {
            transaction {
                val teams: List<ResultRow> = Teams.selectAll().toList()
                println(" Nom \t\t|\tPays")
                println("------------|----------")
                for(row in teams) {
                    println("${row[Teams.name]}    \t| ${row[Teams.firstName]}")
                }
            }
        }
        get("/resultRowFor") {
            transaction {
                val teams: List<ResultRow> = Teams.selectAll().toList()
                for(row in teams) {
                    println("Nom : ${row[Teams.name]}, Responsable : ${row[Teams.firstName]}")
                }
            }
        }
        get("/ResuleRowFinaly") {
            transaction {
                val teams: List<ResultRow> = Teams.selectAll().toList()
                for(row in teams) {
                    val id = row[Teams.id]
                    val name = row[Teams.name]
                    val firstName = row[Teams.firstName]
                    println("ID: $id | Nom: $name | Responsable: $firstName")
                }
            }
        }
        get("/resultRowList") {
            withContext(Dispatchers.IO) {
                val teams = transaction {
                    Teams.selectAll().map { resultRow ->
                        resultRow[Teams.name]
                    }
                }

                call.respondHtml {
                    body {
                        h1 { +"Liste des équipes" }
                        ul {
                            teams.forEach { team ->
                                li { +team.toString() }
                            }
                        }
                    }
                }
            }

        }
        get("/ResultRowSlice") {
            val selectedTeams = transaction {
                Teams.selectAll().map { row ->
                    val id = row[Teams.id].value
                    val name = row[Teams.name]
                    println("ID: $id, Nom: $name")
                }
            }

        }
        get("/insertplayer/manual") {
            transaction {
                Players.insert {
                    it[id] = 3 // ID défini manuellement
                    it[firstName] = "Muriel"
                    it[teamId] = 1

                }
            }
            call.respondText("Joueur inséré avec succès : Alice, ID: 1, Équipe: 1")

        }
        //* http://localhost:8080/insertplayer?id=2&firstName=Bob&teamId=1
        get("/insertplayer/dinamic") {
            val id = call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respondText("ID invalide.")
            val firstName = call.request.queryParameters["firstName"] ?: return@get call.respondText("Nom invalide.")
            val teamId = call.request.queryParameters["teamId"]?.toIntOrNull()
                ?: return@get call.respondText("Team ID invalide.")
            try {
                transaction {
                    Players.insert {
                        it[Players.id] = id
                        it[Players.firstName] = firstName
                        it[Players.teamId] = teamId
                    }
                }
                call.respondText("Joueur inséré avec succès : $firstName, ID: $id, Équipe: $teamId")
            } catch(e: Exception) {
                call.respondText("Erreur lors de l'insertion : ${e.message}")
            }
        }

    }
}