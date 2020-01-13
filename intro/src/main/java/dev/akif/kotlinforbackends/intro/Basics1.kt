package dev.akif.kotlinforbackends.intro

// val creates a final variable. It cannot be assigned another value.
val someNumber: Int = 5

// var creates a variable. It can be reassigned.
// Notice type here is inferred to be a String.
var inferred = "foo"

// fun creates a function/method. It takes a String and returns a String.
fun upper1(text: String): String {
    return text.toUpperCase() // No semicolon is needed
}

// When body is a single expression,
// functions can be defined like values with = and no return.
fun upper2(text: String): String = text.toUpperCase()

// Return type of functions can also be inferred
fun upper3(text: String) = text.toUpperCase()

// Class definitions in Kotlin are final by default (cannot be extended).
// They can be just the definitions when there are no members.
class SimpleClass

// Extendable (non-final) class definition
open class BaseClass
