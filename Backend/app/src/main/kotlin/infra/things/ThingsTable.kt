package infra.things

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ThingsTable : IntIdTable("things") {
    val uuid = text("uuid").uniqueIndex()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
    val serialNumber = text("serial_number").nullable()
    val name = text("name").nullable()
    val model = text("model").nullable()
}