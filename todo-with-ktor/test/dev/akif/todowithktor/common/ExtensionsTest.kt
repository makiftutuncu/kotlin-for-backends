package dev.akif.todowithktor.common

import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertEquals

object ExtensionsTest {
    @Test fun `any value can be lifted into a Maybe`() {
        val expected1: Maybe<String> = Right("foo")
        val expected2: Maybe<Int>    = Right(42)

        val maybe1 = "foo".asMaybe()
        val maybe2 = 42.asMaybe()

        assertEquals(expected1, maybe1)
        assertEquals(expected2, maybe2)
    }

    @Test fun `converting a String to an id can fail when it is not a valid Long`() {
        val expected = TodoError("Invalid id foo!", HttpStatusCode.BadRequest).asMaybe<Long>()

        val actual = "foo".asId()

        assertEquals(expected, actual)
    }

    @Test fun `a String can be converted to an id when it is a valid Long`() {
        val expected = 5L.asMaybe()

        val actual = "5".asId()

        assertEquals(expected, actual)
    }

    @Test fun `a ZonedDateTime can be converted to a formatted String`() {
        val expected = "2020-01-17T15:06:35+03:00"

        val actual = ZonedDateTime.of(2020, 1, 17, 15, 6, 35, 0, ZoneOffset.ofHours(3)).asString()

        assertEquals(expected, actual)
    }

    @Test fun `a formatted date time String can be converted to a ZonedDateTime`() {
        val expected = ZonedDateTime.of(2020, 1, 17, 12, 6, 35, 0, ZoneOffset.UTC)

        val actual = "2020-01-17T12:06:35Z".asZDT()

        assertEquals(expected, actual)
    }
}
