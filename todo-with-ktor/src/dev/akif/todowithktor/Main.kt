package dev.akif.todowithktor

import dev.akif.todowithktor.common.Left
import dev.akif.todowithktor.common.Maybe
import dev.akif.todowithktor.common.Right
import dev.akif.todowithktor.common.ToDoError
import dev.akif.todowithktor.todo.*
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val toDoRepository = ToDoRepository()
    val toDoService    = ToDoService(toDoRepository)
    val toDoController = ToDoController(toDoService)

    install(StatusPages) {
        registerErrorHandler()
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
            registerTypeAdapter(ToDoError::class.java, ToDoError.gsonAdapter)
        }
    }

    install(Routing) {
        get("/ping") {
            call.respondText("pong", ContentType.Text.Html)
        }

        post("/user/{userId}/todo") {
            val userId     = call.parameters["userId"]
            val createToDo = call.receive<CreateToDo>()

            call.respondMaybe(toDoController.create(userId, createToDo), HttpStatusCode.Created)
        }

        get("/user/{userId}/todo") {
            val userId = call.parameters["userId"]

            call.respondMaybe(toDoController.getAllByUserId(userId))
        }

        get("/user/{userId}/todo/{id}") {
            val id     = call.parameters["id"]
            val userId = call.parameters["userId"]

            call.respondMaybe(toDoController.getById(id, userId))
        }

        put("/user/{userId}/todo/{id}") {
            val id         = call.parameters["id"]
            val userId     = call.parameters["userId"]
            val updateToDo = call.receive<UpdateToDo>()

            call.respondMaybe(toDoController.update(id, userId, updateToDo))
        }

        delete("/user/{userId}/todo/{id}") {
            val id     = call.parameters["id"]
            val userId = call.parameters["userId"]

            call.respondMaybe(toDoController.delete(id, userId))
        }
    }
}

suspend fun <T> ApplicationCall.respondMaybe(maybe: Maybe<T>, status: HttpStatusCode = HttpStatusCode.OK) {
    when (maybe) {
        is Left  -> respond(maybe.left.code, maybe.left)
        is Right -> respond(status, maybe.right as Any)
    }
}

fun StatusPages.Configuration.registerErrorHandler() {
    exception<Exception> { cause ->
        call.respondMaybe(ToDoError(cause.message ?: cause.localizedMessage).asMaybe<Unit>())
    }
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
