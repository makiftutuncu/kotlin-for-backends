package dev.akif.todowithktor.common

import dev.akif.todowithktor.database.DB
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception

abstract class Repository<M>(protected val db: DB) {
    abstract fun convertTo(row: ResultRow): M

    fun <T> run(block: () -> T): Maybe<T> =
        try {
            transaction { block().asMaybe() }
        } catch (e: Exception) {
            ToDoError("Database operation failed! Reason: $e", HttpStatusCode.InternalServerError).asMaybe()
        }
}
