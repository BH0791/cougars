package fr.hamtec.data

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
//import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.charLength

//object Teams: IntIdTable("teams"){
////    val id = integer("id").autoIncrement()
//    val name = varchar("name", 128).nullable()
//    init {
//        check {
//            name.charLength().greater(3)
//        }
//        index("name_and_id_index", isUnique = false, id, name)
//    }
////    override val primaryKey = PrimaryKey(id)
//}
object Teams : IntIdTable("teams") {
    val name: Column<String?> = varchar("name", 128).nullable()
    val firstName: Column<String?> = varchar("firstName", 58).nullable()

    init {
        name.charLength().greater(3)
        index(
            customIndexName = "team_custom_id_index",
            isUnique = true,
            id,
            name
        )
    }
}