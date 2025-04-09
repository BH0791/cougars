package fr.hamtec.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.charLength

object Players : Table("players") {
    val id = integer("id").autoIncrement()
    val firstName = varchar("firstName", 50).nullable()
    val teamId = reference("teamid", Teams.id)

    init {
        check {
            firstName.isNotNull() and firstName.charLength().greater(3)
        }
        index(
            customIndexName = "index_team_id",
            isUnique = true,
            teamId,
        )
    }

    override val primaryKey = PrimaryKey(id)
}