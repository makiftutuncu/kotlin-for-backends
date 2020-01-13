package dev.akif.todowithktor

import io.ktor.application.*
import io.ktor.http.ContentType
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("Hello world!", ContentType.Text.Html)
        }
    }
}
