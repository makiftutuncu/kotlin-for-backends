package dev.akif.todowithktor.todo

import dev.akif.todowithktor.common.CRUDRepository
import dev.akif.todowithktor.common.Maybe
import dev.akif.todowithktor.common.asMaybe

class ToDoRepository : CRUDRepository<ToDo>("todo") {
    fun getAllByUserId(userId: Long): Maybe<List<ToDo>> =
        db.entries
          .filter { it.value.userId == userId }
          .map { it.value }
          .toList()
          .asMaybe()
}
