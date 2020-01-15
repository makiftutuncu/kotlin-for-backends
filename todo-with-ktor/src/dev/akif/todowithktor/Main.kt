package dev.akif.todowithktor

import dev.akif.todowithktor.common.ToDoError
import dev.akif.todowithktor.common.ZDT
import dev.akif.todowithktor.common.registerErrorHandler
import dev.akif.todowithktor.database.DB
import dev.akif.todowithktor.todo.ToDoRepository
import dev.akif.todowithktor.todo.ToDoService
import dev.akif.todowithktor.todo.todo
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import java.time.ZonedDateTime

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val db             = DB(if (testing) "mem:todo" else "file:./db/todo.db")
    val toDoRepository = ToDoRepository(db)
    val toDoService    = ToDoService(toDoRepository)

    db.init()

    install(StatusPages) {
        registerErrorHandler()
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
            registerTypeAdapter(ToDoError::class.java, ToDoError.gsonAdapter)
            registerTypeAdapter(ZonedDateTime::class.java, ZDT.gsonAdapter)
        }
    }

    install(Routing) {
        get("/ping") {
            call.respondText("pong", ContentType.Text.Html)
        }

        todo(toDoService)
    }
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
