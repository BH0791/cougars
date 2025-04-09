package fr.hamtec.Routes

import fr.hamtec.data.Players
import fr.hamtec.data.Teams
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureHtmlDslRoute() {
    routing {
        get("/jointures") {
            transaction {
                val result = (Teams innerJoin Players)
                    .select(Teams.name.isNotNull() and (Teams.name like "Fra%"))
                    .map { row ->
                        Pair(
                            row[Players.id],
                            row[Players.firstName] to row[Teams.name]
                        )
                    }

                if(result.isNotEmpty()) {
                    val firstPlayerId = result[0].first // Récupère l'ID du premier joueur
                    println("Premier joueur ID : $firstPlayerId")
                } else {
                    println("Aucun résultat trouvé.")
                }
            }
        }
        get("/jointuresInner") {
            transaction {
                val result = (Teams innerJoin Players)
                    .select(Teams.name, Players.id, Players.firstName) // Spécifie les colonnes nécessaires
                    .where { Teams.name.isNotNull() and (Teams.name like "Fra%") } // Conditions appliquées séparément
                    .map { row ->
                        Pair(
                            row[Players.id],
                            row[Players.firstName] to row[Teams.name]
                        )
                    }

                if(result.isNotEmpty()) {
                    val firstPlayerId = result[0].first
                    println("Premier joueur ID : $firstPlayerId")
                } else {
                    println("Aucun résultat trouvé.")
                }
            }
        }
        get("/affiche") {
            transaction {
                Teams.selectAll().forEach { println("Team : ${it[Teams.name]}") }
                Players.selectAll()
                    .forEach { println("Player : ${it[Players.firstName]} - TeamId : ${it[Players.teamId]}") }
            }
        }
        get("/jointuresEssai") {
            withContext(Dispatchers.IO) {
                transaction {
                    val result = (Teams rightJoin Players)
                        .select(Teams.name, Players.id, Players.firstName)
                        .where { Teams.name.isNotNull() and (Teams.name like "Fra%") }
                        .map { row ->
                            Pair(
                                row[Players.id],
                                row[Players.firstName] to row[Teams.name]
                            )
                        }

                    if(result.isNotEmpty()) {
                        val firstPlayerId = result[0].first
                        println("Premier joueur ID : $firstPlayerId")
                        launch { call.respond("Premier joueur ID : $firstPlayerId") }
                    } else {
                        println("Aucun résultat trouvé.")
                        launch { call.respond("Aucun résultat trouvé.") }
                    }
                }
            }
        }
        get("/jointures/p233") {
            withContext(Dispatchers.IO) {
                transaction {
                    val result = (Teams rightJoin Players)
                        .select(Teams.name, Players.id, Players.firstName)
                        .where { Teams.name.isNotNull() and (Teams.name like "Fra%") }
                        .toList()

                    if(result.isNotEmpty()) {
                        val firstPlayerId = result[0][Players.id]
                        println("Premier joueur ID : $firstPlayerId")
                        launch { call.respond("Premier joueur ID : $firstPlayerId") }
                    } else {
                        println("Aucun résultat trouvé.")
                        launch { call.respond("Aucun résultat trouvé.") }
                    }
                }
            }
        }
    }
}