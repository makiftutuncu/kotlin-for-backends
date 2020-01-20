package dev.akif.todowithktor.common

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.slf4j.Logger
import java.time.ZonedDateTime

fun <T> T.asMaybe(): Maybe<T> = Right(this)

fun String?.asId(): Maybe<Long> =
    try {
        (this ?: "").toLong().asMaybe()
    } catch (e: Exception) {
        ToDoError("Invalid id $this!", HttpStatusCode.BadRequest).asMaybe()
    }

suspend fun <T> ApplicationCall.respondMaybe(maybe: Maybe<T>, status: HttpStatusCode = HttpStatusCode.OK) {
    when (maybe) {
        is Left  -> respond(maybe.left.code, maybe.left)
        is Right -> respond(status, maybe.right as Any)
    }
}

fun StatusPages.Configuration.registerErrorHandler(logger: Logger) {
    exception<Exception> { cause ->
        val error = ToDoError(cause.message ?: cause.localizedMessage)
        logger.error("Request failed! $error", cause)
        call.respondMaybe(error.asMaybe<Unit>())
    }
}

fun ZonedDateTime.asString(): String = this.format(ZDT.formatter)

fun String.asZDT(): ZonedDateTime = ZonedDateTime.parse(this, ZDT.formatter)
