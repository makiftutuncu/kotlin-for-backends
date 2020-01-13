package dev.akif.kotlinforbackends.intro

// Out-of-the-box equals, hashCode, copy, toString methods
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

fun main() {
    // Classic for loop
    for (p in projects) {
        if (p.topAncestor == null) {
            println("Project ${p.name}")
        } else {
            // p.topAncestor is Project here, not Project? (automatic casting)
            println("Child Project ${p.topAncestor.name}")
        }
    }
}
