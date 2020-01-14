package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.Controller
import dev.akif.todowithktor.common.Maybe

class ToDoController(private val service: ToDoService) : Controller() {
    fun create(userIdStr: String?, createToDo: CreateToDo): Maybe<ToDo> =
        userIdStr.asId().flatMap { userId ->
            service.create(userId, createToDo)
        }

    fun getAllByUserId(userIdStr: String?): Maybe<List<ToDo>> =
        userIdStr.asId().flatMap { userId ->
            service.getAllByUserId(userId)
        }

    fun getById(idStr: String?, userIdStr: String?): Maybe<ToDo> =
        idStr.asId().flatMap { id ->
            userIdStr.asId().flatMap { userId ->
                service.getById(id, userId)
            }
        }

    fun update(idStr: String?, userIdStr: String?, updateToDo: UpdateToDo): Maybe<ToDo> =
        idStr.asId().flatMap { id ->
            userIdStr.asId().flatMap { userId ->
                service.update(id, userId, updateToDo)
            }
        }

    fun delete(idStr: String?, userIdStr: String?): Maybe<Unit> =
        idStr.asId().flatMap { id ->
            userIdStr.asId().flatMap { userId ->
                service.delete(id, userId)
            }
        }
}
