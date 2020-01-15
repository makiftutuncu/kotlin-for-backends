package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.*
import dev.akif.todowithktor.database.DB
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.*
import java.time.ZoneOffset

class ToDoRepository(db: DB) : Repository<ToDo>(db) {
    fun create(userId: Long, createToDo: CreateToDo): Maybe<ToDo> =
        run {
            ToDoTable.insertAndGetId {
                it[ToDoTable.userId]  = userId
                it[title]             = createToDo.title
                it[details]           = createToDo.details
                it[time]              = ZDT.now().toInstant()
            }
        }.map { id ->
            ToDo.from(id.value, userId, createToDo)
        }

    fun getAllByUserId(userId: Long): Maybe<List<ToDo>> =
        run {
            ToDoTable
                .select { ToDoTable.userId eq userId }
                .map { row -> ToDoTable.toToDo(row) }
        }

    fun getById(id: Long, userId: Long): Maybe<ToDo?> =
        run {
            ToDoTable
                .select { ToDoTable.id eq id }
                .singleOrNull()
                ?.let { ToDoTable.toToDo(it) }
        }.flatMap { toDo ->
            if (toDo != null && toDo.userId != userId) {
                ToDoError("Todo $id is not found!", HttpStatusCode.NotFound).asMaybe()
            } else {
                toDo.asMaybe()
            }
        }

    fun update(toDo: ToDo, updateToDo: UpdateToDo): Maybe<ToDo> {
        val now = ZDT.now()

        return run {
            ToDoTable.update({ (ToDoTable.id eq toDo.id) }) {
                it[title]   = updateToDo.title ?: toDo.title
                it[details] = updateToDo.details
                it[time]    = now.toInstant()
            }
        }.map {
            toDo.updatedWith(updateToDo).copy(time = now)
        }
    }

    fun delete(id: Long): Maybe<Unit> =
        run<Unit> {
            ToDoTable.deleteWhere {
                ToDoTable.id eq id
            }
        }

    override fun convertTo(row: ResultRow): ToDo  =
        ToDo(
            row[ToDoTable.id].value,
            row[ToDoTable.userId],
            row[ToDoTable.title],
            row[ToDoTable.details],
            row[ToDoTable.time].atZone(ZoneOffset.UTC)
        )
}
