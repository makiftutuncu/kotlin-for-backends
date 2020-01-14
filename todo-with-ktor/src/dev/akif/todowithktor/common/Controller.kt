package dev.akif.todowithktor.common

import io.ktor.http.HttpStatusCode

abstract class Controller {
    fun String?.asId(): Maybe<Long> =
        try {
            (this ?: "").toLong().asMaybe()
        } catch (e: Exception) {
            ToDoError("Invalid id $this!", HttpStatusCode.BadRequest).asMaybe()
        }
}
