package dev.akif.todowithktor.common

abstract class CRUDRepository<M>(protected val name: String) {
    private var nextId: Long = 1L
    protected var db: Map<Long, M> = mapOf()

    fun generateId(): Long {
        val id = nextId
        nextId += 1
        return id
    }

    fun create(id: Long, model: M): Maybe<M> =
        if (db.containsKey(id)) {
            ToDoError("Cannot create $name $id, it already exists!").asMaybe()
        } else {
            db = db + (id to model)
            model.asMaybe()
        }

    fun getById(id: Long): Maybe<M?> = db[id].asMaybe()

    fun update(id: Long, model: M): Maybe<M> =
        if (!db.containsKey(id)) {
            ToDoError("Cannot update $name $id, it does not exist!").asMaybe()
        } else {
            db = db + (id to model)
            model.asMaybe()
        }

    fun delete(id: Long): Maybe<Unit> =
        if (!db.containsKey(id)) {
            ToDoError("Cannot delete $name $id, it does not exist!").asMaybe()
        } else {
            db = db - id
            Unit.asMaybe()
        }
}
