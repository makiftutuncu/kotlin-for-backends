package dev.akif.todowithktor

import com.google.gson.GsonBuilder
import dev.akif.todowithktor.common.*
import dev.akif.todowithktor.database.DB
import dev.akif.todowithktor.todo.ToDoRepository
import dev.akif.todowithktor.todo.ToDoService
import dev.akif.todowithktor.todo.todo
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

val gson =
    GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .registerTypeAdapter(ToDoError::class.java, ToDoError.gsonAdapter)
        .registerTypeAdapter(ZonedDateTime::class.java, ZDT.gsonAdapter)
        .create()

fun Application.modules(db: DB,
                        zdt: ZDTProvider,
                        toDoRepository: ToDoRepository,
                        toDoService: ToDoService) {
    val logger = LoggerFactory.getLogger("todo")

    db.init()

    install(StatusPages) {
        registerErrorHandler(logger)
    }

    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter(gson))
    }

    install(Routing) {
        get("/ping") {
            call.respondText("pong", ContentType.Text.Html)
        }

        todo(toDoService)
    }
}

fun Application.defaultModules() {
    val db             = DB("file:./db/todo.db;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE")
    val zdt            = ZDT
    val toDoRepository = ToDoRepository(db, zdt)
    val toDoService    = ToDoService(toDoRepository)

    modules(db, zdt, toDoRepository, toDoService)
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
