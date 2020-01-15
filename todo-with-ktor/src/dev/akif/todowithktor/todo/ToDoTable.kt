package dev.akif.todowithktor.todo

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.time.ZoneOffset

object ToDoTable : LongIdTable() {
    val userId  = long("user_id")
    val title   = varchar("title", 128)
    val details = varchar("details", 1024).nullable()
    val time    = timestamp("time")

    fun toToDo(row: ResultRow): ToDo =
        ToDo(
            row[id].value,
            row[userId],
            row[title],
            row[details],
            row[time].atZone(ZoneOffset.UTC)
        )
}
