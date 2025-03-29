package fr.hamtec.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.charLength

object Teams: Table("teams"){
    val id = integer("id")
    val name = varchar("name", 128).nullable()
    init {
        check {
            name.charLength().greater(3)
        }
        index("name_and_id_index", isUnique = false, id, name)
    }
    override val primaryKey = PrimaryKey(id)
}