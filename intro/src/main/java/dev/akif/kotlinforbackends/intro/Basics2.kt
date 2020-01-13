package dev.akif.kotlinforbackends.intro

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

fun main() {
    Singleton.foo() // Singleton method call

    // No need for new keyword when creating instances
    Cat("Cookie", 2).moveTo(1, 2)

    // Does not compile
    /*
    val a = Animal("foo")   // As Animal is abstract
    Cat.adopt("Peanut").age // As age is not a property
    */
}
