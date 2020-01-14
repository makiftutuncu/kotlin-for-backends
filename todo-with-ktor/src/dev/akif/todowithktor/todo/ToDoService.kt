package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.Maybe
import dev.akif.todowithktor.common.ToDoError
import dev.akif.todowithktor.common.asMaybe
import io.ktor.http.HttpStatusCode

class ToDoService(private val repository: ToDoRepository) {
    fun create(userId: Long, createToDo: CreateToDo): Maybe<ToDo> {
        val id   = repository.generateId()
        val toDo = ToDo.from(id, userId, createToDo)

        return repository.create(id, toDo)
    }

    fun getById(id: Long, userId: Long): Maybe<ToDo> {
        val maybeToDo = repository.getById(id)

        return maybeToDo.flatMap { toDo ->
            if (toDo == null || toDo.userId != userId) {
                ToDoError("Cannot get todo $id, it does not exist!", HttpStatusCode.NotFound).asMaybe()
            } else {
                toDo.asMaybe()
            }
        }
    }

    fun getAllByUserId(userId: Long): Maybe<List<ToDo>> = repository.getAllByUserId(userId)

    fun update(id: Long, userId: Long, updateToDo: UpdateToDo): Maybe<ToDo> {
        return getById(id, userId).flatMap { toDo ->
            val newToDo = toDo.updatedWith(updateToDo)

            repository.update(toDo.id, newToDo)
        }
    }

    fun delete(id: Long, userId: Long): Maybe<Unit> {
        return getById(id, userId).flatMap { toDo ->
            repository.delete(toDo.id)
        }
    }
}
