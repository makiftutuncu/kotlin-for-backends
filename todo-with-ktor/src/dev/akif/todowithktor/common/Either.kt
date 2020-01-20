package dev.akif.todowithktor.common

sealed class Either<L, R>(open val left: L?, open val right: R?) {
    fun <T> map(f: (R) -> T): Either<L, T> = flatMap { r -> Right<L, T>(f(r)) }

    fun <T> flatMap(f: (R) -> Either<L, T>): Either<L, T> =
        when (this) {
            is Left  -> Left(left)
            is Right -> f(right)
        }

    fun <T> fold(fromLeft: (L) -> T, fromRight: (R) -> T): T =
        when (this) {
            is Left  -> fromLeft(left)
            is Right -> fromRight(right)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        return when (other) {
            is Left<*, *> -> {
                val that: Left<*, *> = other
                this.left == that.left
            }
            is Right<*, *> -> {
                val that: Right<*, *> = other
                this.right == that.right
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return (left?.hashCode() ?: 0) + (right?.hashCode() ?: 0)
    }
}

class Left<L, R>(override val left: L) : Either<L, R>(left, null) {
    override fun toString(): String = "Left($left)"
}

class Right<L, R>(override val right: R) : Either<L, R>(null, right) {
    override fun toString(): String = "Right($right)"
}

typealias Maybe<T> = Either<TodoError,  T>
