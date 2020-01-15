package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.ZDT
import java.time.ZonedDateTime

data class ToDo(val id: Long = -1L,
                val userId: Long,
                val title: String,
                val details: String? = null,
                val time: ZonedDateTime = ZDT.now()) {
    fun updatedWith(updateToDo: UpdateToDo): ToDo = copy(title = updateToDo.title ?: title, details = updateToDo.details)

    companion object {
        fun from(id: Long, userId: Long, createToDo: CreateToDo): ToDo = ToDo(id, userId, createToDo.title, createToDo.details)
    }
}

data class CreateToDo(val title: String,
                      val details: String? = null)

data class UpdateToDo(val title: String? = null,
                      val details: String? = null)
