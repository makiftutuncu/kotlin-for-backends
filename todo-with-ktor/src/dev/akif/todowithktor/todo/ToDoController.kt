package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.asId
import dev.akif.todowithktor.common.Maybe
import dev.akif.todowithktor.common.respondMaybe
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.routing.*

fun Route.todo(service: ToDoService) {
    val controller = ToDoController(service)

    route("/todo") {
        post("/{userId}") {
            val userId     = call.parameters["userId"]
            val createToDo = call.receive<CreateToDo>()

            call.respondMaybe(controller.create(userId, createToDo), HttpStatusCode.Created)
        }

        get("/{userId}") {
            val userId = call.parameters["userId"]

            call.respondMaybe(controller.getAllByUserId(userId))
        }

        get("/{userId}/{id}") {
            val id     = call.parameters["id"]
            val userId = call.parameters["userId"]

            call.respondMaybe(controller.getById(id, userId))
        }

        put("/{userId}/{id}") {
            val id         = call.parameters["id"]
            val userId     = call.parameters["userId"]
            val updateToDo = call.receive<UpdateToDo>()

            call.respondMaybe(controller.update(id, userId, updateToDo))
        }

        delete("/{userId}/{id}") {
            val id     = call.parameters["id"]
            val userId = call.parameters["userId"]

            call.respondMaybe(controller.delete(id, userId))
        }
    }
}

class ToDoController(val service: ToDoService) {
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
