package infra.things

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ThingsTable : IntIdTable("things") {
    val uuid = text("uuid").uniqueIndex()
    val serialNumber = text("serial_number").uniqueIndex()
    val name = text("name").uniqueIndex()
    val model = text("model")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}