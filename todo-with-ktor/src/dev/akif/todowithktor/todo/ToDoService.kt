package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.Maybe
import dev.akif.todowithktor.common.ToDoError
import dev.akif.todowithktor.common.asMaybe
import io.ktor.http.HttpStatusCode

class ToDoService(private val repository: ToDoRepository) {
    fun create(userId: Long, createToDo: CreateToDo): Maybe<ToDo> = repository.create(userId, createToDo)

    fun getAllByUserId(userId: Long): Maybe<List<ToDo>> = repository.getAllByUserId(userId)

    fun getById(id: Long, userId: Long): Maybe<ToDo> =
        repository.getById(id, userId).flatMap { toDo ->
            toDo?.asMaybe() ?: ToDoError("Todo $id is not found!", HttpStatusCode.NotFound).asMaybe()
        }

    fun update(id: Long, userId: Long, updateToDo: UpdateToDo): Maybe<ToDo> =
        getById(id, userId).flatMap { toDo ->
            repository.update(toDo, updateToDo)
        }

    fun delete(id: Long, userId: Long): Maybe<Unit> =
        getById(id, userId).flatMap { toDo ->
            repository.delete(toDo.id)
        }
}
