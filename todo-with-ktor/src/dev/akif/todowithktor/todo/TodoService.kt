package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.Maybe
import dev.akif.todowithktor.common.TodoError
import dev.akif.todowithktor.common.asMaybe
import io.ktor.http.HttpStatusCode

class TodoService(val repository: TodoRepository) {
    fun create(userId: Long, createTodo: CreateTodo): Maybe<Todo> = repository.create(userId, createTodo)

    fun getAllByUserId(userId: Long): Maybe<List<Todo>> = repository.getAllByUserId(userId)

    fun getById(id: Long, userId: Long): Maybe<Todo> =
        repository.getById(id, userId).flatMap { todo ->
            todo?.asMaybe() ?: TodoError("Todo $id is not found!", HttpStatusCode.NotFound).asMaybe()
        }

    fun update(id: Long, userId: Long, updateTodo: UpdateTodo): Maybe<Todo> =
        getById(id, userId).flatMap { todo ->
            repository.update(todo, updateTodo)
        }

    fun delete(id: Long, userId: Long): Maybe<Unit> =
        getById(id, userId).flatMap { todo ->
            repository.delete(todo.id)
        }
}
