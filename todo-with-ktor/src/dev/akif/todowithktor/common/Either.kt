package dev.akif.todowithktor.common

sealed class Either<L, R>(open val left: L?, open val right: R?) {
    fun <T> map(f: (R) -> T): Either<L, T> = flatMap { r -> Right<L, T>(f(r)) }

    fun <T> flatMap(f: (R) -> Either<L, T>): Either<L, T> =
        when (this) {
            is Left  -> Left(left)
            is Right -> f(right)
        }
}

class Left<L, R>(override val left: L) : Either<L, R>(left, null)

class Right<L, R>(override val right: R) : Either<L, R>(null, right)

typealias Maybe<T> = Either<ToDoError,  T>
