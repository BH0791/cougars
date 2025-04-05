package fr.hamtec.Routes

import fr.hamtec.data.Teams
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureCRUD(){
    routing {
        /**
         * Fonctionnement de ResultRow :
         * Lorsque tu fais une sélection avec Teams.selecteAll(), cela retourne un ResultSet de la base de données.
         *  Chaque ligne de ce ResultSet est encapsulée dans un ResultRow, ce qui te permet de travailler avec les données de façon typée et sécurisée
         */
        get("/resultRow"){
            transaction{
                val teams: List<ResultRow> = Teams.selectAll().toList()
                println(" Nom \t\t|\tPays")
                println("------------|----------")
                for (row in teams) {
                    println("${row[Teams.name]}    \t| ${row[Teams.firstName]}")
                }
            }
        }
        /**Fonctionnement de ResultRow
         * Lorsque tu fais une sélection avec Teams.selectAll(), cela retourne un ResultSet de la base de données.
         * Chaque ligne de ce ResultSet est encapsulée dans un ResultRow, ce qui te permet de travailler avec les données de façon typée et sécurisée
         */
        get("/resultRowFor"){
            transaction{
                val teams: List<ResultRow> = Teams.selectAll().toList()
                for (row in teams) {
                    println("Nom : ${row[Teams.name]}, Responsable : ${row[Teams.firstName]}")
                }
            }
        }
        get("/ResuleRowFinaly"){
            transaction{
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