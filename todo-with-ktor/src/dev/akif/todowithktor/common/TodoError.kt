package dev.akif.todowithktor.common

import com.google.gson.JsonObject
import com.google.gson.JsonSerializer
import io.ktor.http.HttpStatusCode

data class TodoError(val reason: String, val code: HttpStatusCode = HttpStatusCode.InternalServerError) {
    fun <T> asMaybe(): Maybe<T> = Left(this)

    companion object {
        val gsonAdapter = JsonSerializer<TodoError> { error, _, _ ->
            JsonObject().apply {
                addProperty("code", error.code.value)
                addProperty("message", error.reason)
            }
        }
    }
}
