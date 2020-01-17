package dev.akif.todowithktor.common

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

object EitherTest {
    @Test fun `Left stays the same when mapped`() {
        val error = "Value is not an integer"
        val maybe = Left<String, Int>(error)

        val expected = Left<String, Int>(error)
        val actual   = maybe.map { it * 2 }

        assertEquals(expected, actual)
    }

    @Test fun `Left stays the same when flatMapped`() {
        val error = "Value is not an integer"
        val maybe = Left<String, Int>(error)

        val expected = Left<String, Int>(error)
        val actual1  = maybe.flatMap { Left<String, Int>("This is another error") }
        val actual2  = maybe.flatMap { Right<String, Int>(it * 3) }

        assertEquals(expected, actual1)
        assertEquals(expected, actual2)
    }

    @Test fun `a new Right is produced when Right is mapped`() {
        val maybe = Right<Int, Int>(42)

        val expected = Right<Int, String>("Meaning of life is 42")
        val actual   = maybe.map { "Meaning of life is $it" }

        assertEquals(expected, actual)
    }

    @Test fun `a new Left is produced when Right is flatMapped to a Left`() {
        val maybe = Right<Int, Int>(42)

        val expected = Left<Int, String>(-1)
        val actual   = maybe.flatMap { Left<Int, String>(-1) }

        assertEquals(expected, actual)
    }

    @Test fun `a new Right is produced when Right is flatMapped to a Right`() {
        val maybe = Right<Int, Int>(42)

        val expected = Right<Int, String>("Meaning of life is 42")
        val actual   = maybe.flatMap { Right<Int, String>("Meaning of life is $it") }

        assertEquals(expected, actual)
    }
}
