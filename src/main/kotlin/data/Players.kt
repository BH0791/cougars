package fr.hamtec.data

import org.jetbrains.exposed.sql.Table

object Players: Table("players") {
    val id = integer("id")
    val firstName = varchar("first_Name", 50)
    val teamId = reference("team_id", Teams.id)
}