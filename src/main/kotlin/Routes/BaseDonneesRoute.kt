package fr.hamtec.Routes

//import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import fr.hamtec.FileHeadlog.logger
import fr.hamtec.data.Teams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configDBRoute() {
    routing {
        get("/teams") {
            val teams = transaction {
                Teams.selectAll().map { "-*-" + it[Teams.name] }
            }
            call.respond(teams)
        }
        get("/aff1") {
            val teams = transaction {
                Teams.selectAll().map { it[Teams.name] }
            }
            call.respond(teams) // Retourne la liste directement.
        }
        get("/affiche_1") {
            try {
                val teams = transaction {
                    Teams.selectAll().mapNotNull { it[Teams.name] } // Ignore les nulls, si pertinents.
                }

                call.respond(mapOf("teams" to teams))
            } catch(ex: Exception) {
                logger.error("Erreur lors de la récupération des équipes : ${ex.message}")
                call.respondText("Erreur interne du serveur", status = HttpStatusCode.InternalServerError)
            }
        }
        get("/affiche-html") {
            val teams = transaction {
                Teams.selectAll().map { it[Teams.id] to it[Teams.name] }
            }

            call.respondHtml {
                body {
                    h1 { +"Liste des équipes" }
                    table {
                        tr {
                            th { +"ID" }
                            th { +"Nom" }
                        }
                        teams.forEach { (id, name) ->
                            tr {
                                td { +id.toString() }
                                td { +name.toString() }
                            }
                        }
                    }
                }
            }
        }
        get("/affiche_2") {
            val teams = transaction {
                Teams.selectAll().map {
                    it[Teams.id] to it[Teams.name]
                }
            }

            if (teams.isEmpty()) {
                call.respondText("Aucune équipe trouvée.", status = HttpStatusCode.NoContent)
            } else {
                val result = teams.joinToString("\n") { (id, name) ->
                    "| ${id ?: "NULL"} | ${name ?: "NULL"} |"
                }
                val header = "| ID     | Nom       |\n|--------|-----------|"
                call.respondText("$header\n$result", contentType = ContentType.Text.Plain)
            }
        }
        get("/affiche") {
            logger.info("Routes déclenchées : récupération des équipes.")
            val teams = transaction {
                Teams.selectAll().mapNotNull { it[Teams.name] } // Ignore les nulls, si pertinents.
            }
            logger.info("Equipes. $teams")
            if(teams.isEmpty()) {
                call.respondText("Aucune équipe trouvée.", status = HttpStatusCode.NoContent)
            } else {
                call.respond(mapOf("teams" to teams))
                //call.respond(teams) // Retourne la liste, même vide.
            }
        }
        get("/affiche3") {
            val teams = transaction {
                Teams.selectAll().map { resultRow ->
                    mapOf(
                        "name" to resultRow[Teams.name],
                        "firstName" to resultRow[Teams.firstName]
                    )
                }
            }
            call.respond(teams) // Cela retourne une liste sérialisable
        }
        get("/affiche4") {
            val teams: List<ResultRow> = transaction {
                Teams.selectAll().toList()
            }

            // Format JSON à retourner
            val formattedTeams = teams.map { resultRow ->
                mapOf(
                    "name" to resultRow[Teams.name],
                    "firstName" to resultRow[Teams.firstName] // Inclure firstName si pertinent
                )
            }

            call.respond(formattedTeams)
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

            val teams = withContext(Dispatchers.IO) {

                val teamName = transaction {
                    //exec("SET search_path TO public")
                        Teams.select(Teams.id eq 1)
                            .map { it[Teams.name] }
                            .singleOrNull()
                }
                call.respond("$teamName")
            }
        }
        get("/insertAndGetId") {
            transaction {
                val id = Teams.insertAndGetId {
                    it[name] = "Laos"
                    it[firstName] = "laotien"
                }
            }
            call.respond("Insertion réusit")
        }
        get("/selectAll") {
            transaction {
                val teams: List<ResultRow> = Teams.selectAll().toList()
                teams.forEach { resultRow ->
                    println("ID: ${resultRow[Teams.id]}, Name: ${resultRow[Teams.name]}, FirstName: ${resultRow[Teams.firstName]}")
                }
                // Mapper chaque ligne en un format sérialisable
                val formattedTeams = teams.map { resultRow ->
                    mapOf(
                        "ID" to resultRow[Teams.id].value, // Extraire la valeur brute de EntityID
                        "Name" to resultRow[Teams.name],
                        "FirstName" to resultRow[Teams.firstName]
                    )
                }

            }
            val formattedTeams: List<Map<String, Any?>> = transaction {
                Teams.selectAll().map { resultRow ->
                    mapOf(
                        "Name" to resultRow[Teams.name],
                        "FirstName" to resultRow[Teams.firstName]  // Gérer les valeurs nulles
                    )
                }
            }
            call.respond(formattedTeams)
        }
        // +Exemple : Lecture/écriture avec
        // +Voici comment utiliser  pour rendre les interactions avec la base de données plus efficaces dans Ktor
        get("/teams") {
            affPrint()
            // Déplace la transaction sur Dispatcher.IO
            val teams = withContext(Dispatchers.IO) {
                transaction {
                    Teams.selectAll().map { resultRow ->
                        mapOf(
                            "Name" to resultRow[Teams.name],
                            "FirstName" to resultRow[Teams.firstName]
                        )
                    }
                }
            }

            // call.respond reste en dehors du contexte transactionnel
            call.respond(teams)
        }
        get("/teams2") {
            affPrint()
            // Déplace la transaction sur Dispatcher.IO
            val teams = withContext(Dispatchers.IO) {
                transaction {
                    Teams.selectAll().map { resultRow ->
                        resultRow[Teams.name]
                    }
                }
            }

            // call.respond reste en dehors du contexte transactionnel
            call.respond(teams)
        }
        get("/insert/{id_name}") {
            val id_name = call.parameters["id_name"]
            val teams = withContext(Dispatchers.IO) {
                transaction {
                    Teams.insert {
                        it[name] = id_name
                        it[firstName] = "Italien"
                    }
                }
            }
        }
        get("/where1") {
            affPrint()
            // Déplace la transaction sur Dispatcher.IO
            val teams = withContext(Dispatchers.IO) {
                transaction {
                    Teams.selectAll().where { Teams.name like "Fra%" }
                }
            }
            // call.respond reste en dehors du contexte transactionnel
            call.respond(teams)
        }
        get("/where") {
            affPrint()

            // Déplace la transaction sur Dispatcher.IO
            val teams = withContext(Dispatchers.IO) {
                transaction {
                    // Utilisation correcte du filtre
                    Teams.selectAll().where { Teams.name like "Fra%" }.map { resultRow ->
                        mapOf(
                            "Name" to resultRow[Teams.name],
                            "FirstName" to resultRow[Teams.firstName]
                        )
                    }
                }
            }

            // call.respond avec une liste sérialisable
            call.respond(teams)
        }
        get("/login") {
            val pays: String? = call.queryParameters["name"]
            val habs: String? = call.queryParameters["firstName"]
            val teams = withContext(Dispatchers.IO) {
                transaction {
                    val teams = Teams.insertAndGetId {
                        it[name] = pays
                        it[firstName] = habs
                    }
                }
            }
            call.respondHtml {
                head { title { +"Valide" } }
                body {
                    p { +"Les valeurs [$pays] [$habs] sont validées." }
                }
            }


        }
        get("/nouveau") {
            call.respondHtml {
                head { title { +"nouveau" } }
                body {
                    form(action = "/login") {
                        p {
                            +"Name:"
                            textInput(name = "name")
                        }
                        p {
                            +"firstName:"
                            textInput(name = "firstName")
                        }
                        p {
                            submitInput() { value = "Valider" }
                        }
                    }
                }
            }
        }
        get("/resultRow") {
            transaction {

                val teams: List<ResultRow> = Teams.selectAll().toList()

                for (row in teams) {
                    val id = row[Teams.id]
                    val name = row[Teams.name]
                    val firstName = row[Teams.firstName]
                    println("ID: $id | Nom: $name | Responsable: $firstName")
                }
            }
        }

    }
}

fun affPrint() {
    transaction {
        val teams: List<ResultRow> = Teams.selectAll().toList()
        teams.forEach { resultRow ->
            logger.info("ID: ${resultRow[Teams.id]}, Name: ${resultRow[Teams.name]}, FirstName: ${resultRow[Teams.firstName]}")
        }
    }
}