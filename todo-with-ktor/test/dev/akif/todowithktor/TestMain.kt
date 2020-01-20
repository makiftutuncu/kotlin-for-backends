package dev.akif.todowithktor

import dev.akif.todowithktor.common.FixedZDT
import dev.akif.todowithktor.common.ZDT
import dev.akif.todowithktor.database.DB
import dev.akif.todowithktor.todo.TodoRepository
import dev.akif.todowithktor.todo.TodoService
import io.ktor.application.Application
import kotlin.random.Random

val db             = DB("mem:todo${Random.nextInt(1, 1000)}")
val zdt            = FixedZDT(ZDT.now())
val todoRepository = TodoRepository(db, zdt)
val todoService    = TodoService(todoRepository)

fun Application.testModules() {
    return modules(db, zdt, todoRepository, todoService)
}
