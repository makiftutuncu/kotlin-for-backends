# Kotlin for Backends

Code examples

## Basic Concepts

```kotlin
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

interface CanMove { fun moveTo(x: Int, y: Int) } // Interface definition

object Singleton { fun foo() {} } // Out-of-the-box singleton

// name is both a constructor parameter and an overridable property
abstract class Animal(open val name: String)

// Class with primary constructor extending Animal and implementing CanMove
// age here is just a constructor parameter
class Cat(override val name: String, age: Int) : Animal(name), CanMove {
   private val _age: Int = age // Property, e.g. instance field

   // override is keyword
   override fun moveTo(x: Int, y: Int) = println(
     "$name ($_age) is moving to ($x,$y)" // Cool string interpolation
   )

   companion object {
       // Like a static method in Java
       fun adopt(name: String): Cat = Cat(name, 1)
   }
}

Singleton.foo() // Singleton method call

// No need for new keyword when creating instances
Cat("Cookie", 2).moveTo(1, 2)

// Does not compile
val a = Animal("foo")   // As Animal is abstract
Cat.adopt("Peanut").age // As age is not a property

// Out-of-the-box equals, hashCode, copy, toString methods
// Great for classes that just hold data (e.g. POJO)
// parent is nullable and has a default value of null
data class Project(val name: String, val parent: Project? = null) {
   // safe access operator (returns null instead of NPE)
   val topAncestor: Project? = parent?.topAncestor
}

// No need for parent argument because it has a default value
val sahibinden = Project("sahibinden.com")

// Arguments can be passed by name, order is not important
val arama = Project(parent = sahibinden, name = "Arama")

// Immutable list constructor
val projects = listOf(
   sahibinden,
   arama,
   Project("Ilan", sahibinden),
   Project("Kategori", arama)
)

// Classic for loop
for (p in projects) {
   if (p.topAncestor == null) {
       println("Project ${p.name}")
   } else {
       // p.topAncestor is Project here, not Project? (automatic casting)
       println("Child Project ${p.topAncestor.name}")
   }
}
```

## More Advanced Concepts

```kotlin
// Sealed classes cannot be instantiated, they are great for ADTs
// They can only be extended in this file
sealed class Color(val r: Short, val g: Short, val b: Short) {
   // when is like switch in Java but it can match to types, patterns etc.
   fun toHex() = when(this) {
       is Red   -> "FF0000"
       is Green -> "00FF00"
       is Blue  -> "0000FF"
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

val r = Red.toHex()

// toColor() acts like it is an existing String method
// ?: is called Elvis operator, yields right if left is null
val x = "123456".toColor() ?: "Unknown"
```

