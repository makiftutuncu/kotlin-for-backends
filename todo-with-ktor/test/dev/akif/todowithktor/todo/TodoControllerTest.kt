package dev.akif.todowithktor.todo

import com.google.gson.reflect.TypeToken
import dev.akif.todowithktor.*
import dev.akif.todowithktor.common.TodoError
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

object TodoControllerTest {
    private val now = zdt.now()

    private val todoType      = Todo::class.java
    private val todoErrorType = TodoError::class.java
    private val todoListType  = (object : TypeToken<List<Todo>>() {}).type

    @Test fun `create a new to-do item`() = withTestApplication(Application::testModules) {
        val call = handleRequest(HttpMethod.Post, "/todo/1") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(gson.toJson(CreateTodo("Test Title", "Test Details")))
        }

        with(call) {
            val actual = gson.fromJson<Todo>(response.content, todoType)

            val expected = Todo(actual.id, 1L, "Test Title", "Test Details", now)

            assertEquals(HttpStatusCode.Created, response.status())
            assertEquals(expected, actual)
        }
    }

    @Test fun `get nothing when there is no to-do items of a user`() = withTestApplication(Application::testModules) {
        val randomId = Random.nextInt()

        with(handleRequest(HttpMethod.Get, "/todo/$randomId")) {
            val expected = listOf<Todo>()

            val actual = gson.fromJson<List<Todo>>(response.content, todoListType)

            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expected, actual)
        }

    }

    @Test fun `get all to-do items of a user`() = withTestApplication(Application::testModules) {
        val ids = listOf(
            CreateTodo("Test Title 1", "Test Details 1"),
            CreateTodo("Test Title 2", "Test Details 2")
        ).map { create ->
            todoService
                .create(1L, create)
                .map { it.id }
                .fold({ -1L }, { it } )
        }

        with(handleRequest(HttpMethod.Get, "/todo/1")) {
            val expected = listOf(
                Todo(ids[0], 1L, "Test Title 1", "Test Details 1", now),
                Todo(ids[1], 1L, "Test Title 2", "Test Details 2", now)
            )

            val actual = gson.fromJson<List<Todo>>(response.content, todoListType)

            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expected, actual)
        }
    }

    @Test fun `fail to get a non-existing to-do item of a user`() = withTestApplication(Application::testModules) {
        val randomId = Random.nextInt()

        with(handleRequest(HttpMethod.Get, "/todo/1/$randomId")) {
            val expected = gson.toJson(TodoError("Todo $randomId is not found!", HttpStatusCode.NotFound), todoErrorType)

            assertEquals(HttpStatusCode.NotFound, response.status())
            assertEquals(expected, response.content)
        }
    }

    @Test fun `get a to-do item of a user`() = withTestApplication(Application::testModules) {
        val id = todoService
            .create(1L, CreateTodo("Test Title", "Test Details"))
            .map { it.id }
            .fold({ -1L }, { it })

        with(handleRequest(HttpMethod.Get, "/todo/1/$id")) {
            val expected = Todo(id, 1L, "Test Title", "Test Details", now)

            val actual = gson.fromJson<Todo>(response.content, todoType)

            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expected, actual)
        }
    }

    @Test fun `fail to update a non-existing to-do item of a user`() = withTestApplication(Application::testModules) {
        val randomId = Random.nextInt()

        val call = handleRequest(HttpMethod.Put, "/todo/1/$randomId") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(gson.toJson(UpdateTodo("Test Title 2", "Test Details 2")))
        }

        with(call) {
            val expected = gson.toJson(TodoError("Todo $randomId is not found!", HttpStatusCode.NotFound), todoErrorType)

            assertEquals(HttpStatusCode.NotFound, response.status())
            assertEquals(expected, response.content)
        }
    }

    @Test fun `update a to-do item of a user`() = withTestApplication(Application::testModules) {
        val id = todoService
            .create(1L, CreateTodo("Test Title 1", "Test Details 1"))
            .map { it.id }
            .fold({ -1L }, { it })

        val call = handleRequest(HttpMethod.Put, "/todo/1/$id") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(gson.toJson(UpdateTodo("Test Title 2", "Test Details 2")))
        }

        with(call) {
            val expected = Todo(id, 1L, "Test Title 2", "Test Details 2", now)

            val actual = gson.fromJson<Todo>(response.content, todoType)

            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expected, actual)
        }
    }

    @Test fun `fail to delete a non-existing to-do item of a user`() = withTestApplication(Application::testModules) {
        val randomId = Random.nextInt()

        with(handleRequest(HttpMethod.Delete, "/todo/1/$randomId")) {
            val expected = gson.toJson(TodoError("Todo $randomId is not found!", HttpStatusCode.NotFound), todoErrorType)

            assertEquals(HttpStatusCode.NotFound, response.status())
            assertEquals(expected, response.content)
        }
    }

    @Test fun `delete a to-do item of a user`() = withTestApplication(Application::testModules) {
        val id = todoService
            .create(1L, CreateTodo("Test Title 1", "Test Details 2"))
            .map { it.id }
            .fold({ -1L }, { it })

        with(handleRequest(HttpMethod.Delete, "/todo/1/$id")) {
            val expected = "{}"

            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expected, response.content)
        }
    }
}
