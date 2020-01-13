package dev.akif.kotlinforbackends.intro

// Sealed classes cannot be instantiated, they are great for ADTs
// They can only be extended in this file
sealed class Color(val r: Short, val g: Short, val b: Short) {
    // when is like switch in Java but it can match to types, patterns etc.
    fun toHex() = when(this) {
        Red   -> "FF0000"
        Green -> "00FF00"
        Blue  -> "0000FF"
    }
}

// Singleton instances of colors
object Red : Color(255, 0, 0)
object Green : Color(0, 255, 0)
object Blue : Color(0, 0, 255)

// Extension function to type String
// this refers to the String instance on which function is called
fun String.toColor(): Color? = when(this) {
    "FF0000" -> Red
    "00FF00" -> Green
    "0000FF" -> Blue
    else     -> null
}

fun main() {
    val r = Red.toHex()

    // toColor() acts like it is an existing String method
    // ?: is called Elvis operator, yields right if left is null
    val x = "123456".toColor() ?: "Unknown"
}
