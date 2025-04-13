package fr.hamtec.Routes

import fr.hamtec.FileHeadlog.logger
import fr.hamtec.data.Players
import fr.hamtec.data.Teams
import io.ktor.server.application.Application
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.title
import kotlinx.html.tr
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
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
        get("/jointuresInnerImplicite") {
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
        get("/affiche/teams") {
            withContext(Dispatchers.IO){
                //+Afficher le nom du withContext
                logger.info("Contexte courant : ${coroutineContext[CoroutineDispatcher]}")
                val result = transaction {
                    Teams.select(Teams.id, Teams.name)
                        .toList()
                }
                call.respondHtml{
                    head {
                        title { +"Résultats de la Jointure" }
                    }
                    body {
                        style = "width: 100%; text-align: center;"
                        h1 { +"Résultats de la Jointure INNER entre Teams et Players" }
                        if (result.isNotEmpty()) {
                            table {
                                style = "border-block:  1px dashed blue; border-spacing: 5px; width: 100%; text-align: center;"
                                tr {
                                    th { +"Player ID" }
                                    th { +"Team Name" }
                                }
                                result.forEach { row ->
                                    tr {
                                        td { +row[Teams.id].toString() }
                                        td { +row[Teams.name]!! }
                                    }
                                }
                            }
                        } else {
                            p { +"Aucun résultat trouvé." }
                        }
                    }
                }

            }
        }
        get("/jointuresInnerExplicite") {
            withContext(Dispatchers.IO) {
                val result: List<ResultRow> = transaction {
                    Teams.join(
                        otherTable = Players,
                        joinType = JoinType.INNER,
                        onColumn = Teams.id,
                        otherColumn = Players.teamId
                    )
                        .select(Players.id, Players.firstName, Teams.name)
                        .toList()
                }

                // Génération de la réponse HTML avec HTML DSL
                call.respondHtml {
                    head {
                        title { +"Résultats de la Jointure" }
                    }
                    body {
                        h1 { +"Résultats de la Jointure INNER entre Teams et Players" }
                        if (result.isNotEmpty()) {
                            table {
                                style = "border-collapse: collapse; width: 100%; text-align: left;"
                                tr {
                                    th { +"Player ID" }
                                    th { +"First Name" }
                                    th { +"Team Name" }
                                }
                                result.forEach { row ->
                                    tr {
                                        td { +row[Players.id].toString() }
                                        td { +row[Players.firstName]!! }
                                        td { +row[Teams.name]!! }
                                    }
                                }
                            }
                        } else {
                            p { +"Aucun résultat trouvé." }
                        }
                    }
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