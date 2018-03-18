package com.gamasoft.arrow.monads


data class User(val name: String)

class Context(val connectionUrl: String) {

    fun getUser(id: String): User {

        println("getUser $id from $connectionUrl")
        val db = openDb(connectionUrl)
        return db.fetchUser(id)
    }

    private fun openDb(connectionUrl: String): Db {
        return Db.connect(connectionUrl)
    }

    fun displayUserOnScreen(u: User){
        println("User name ${u.name}")
    }

}

class Db {
    companion object {
        fun connect(connectionUrl: String): Db {
           return Db()
        }

    }

    fun fetchUser(id: String): User {
        return User(id)
    }

}
