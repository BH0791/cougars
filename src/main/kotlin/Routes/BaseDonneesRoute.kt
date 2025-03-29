package fr.hamtec.Routes

import fr.hamtec.data.Teams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configDBRoute(){
    routing {
        get("/aff") {

            transaction {

                val teams: List<ResultRow> = Teams.selectAll().toList()

                launch {
                    call.respondText { "${teams.elementAt(2)}" }
                }
            }
        }
    }
}