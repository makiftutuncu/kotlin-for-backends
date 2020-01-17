package dev.akif.todowithktor.todo

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

object ToDoControllerTest {
    @Test fun `create a new to-do item`() = withTestApplication {
        val expected = """{}"""

        val call = handleRequest(HttpMethod.Post, "/todo/1") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"title":"Test Title","details":"Test Details"}""")
        }

        with(call) {
            val actual = response.content

            assertEquals(HttpStatusCode.Created, response.status())
            assertEquals(expected, actual)
        }
    }
}
