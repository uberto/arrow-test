package com.gamasoft.arrow.monads


data class User(val name: String)

class Context(val connectionUrl: String) {

    fun getUser(id: String): User {

        println("getUser $id from $connectionUrl")
        val db = openDb(connectionUrl)
        return db.fetchUser(id)
    }

    private fun openDb(connectionUrl: String): OpenDb {
        return Db.connect(connectionUrl)
    }

    fun displayUserOnScreen(u: User){
        println("User name ${u.name}")
    }

    fun findCommonFriends(u1: User, u2: User): List<User> {
        val db = openDb(connectionUrl)
        return db.findCommonFriends(u1, u2)
    }

}

sealed class Db {
    companion object {
        fun connect(connectionUrl: String): OpenDb {
            return OpenDb()
        }
    }
}

object ClosedDb: Db() {

}

class OpenDb: Db() {

    fun fetchUser(id: String): User {
        return User(id)
    }

    fun findCommonFriends(u1: User, u2: User): List<User> {
        return listOf(User(u1.name + "_f"), User(u2.name + "_f"))
    }

}
