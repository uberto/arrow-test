package com.gamasoft.extfun

typealias PersonId = Int

data class Person(val name: String, val id: PersonId, val friends: MutableSet<Person> = mutableSetOf()) {

    fun beFriend(another: Person) {
        friends.add(another)
    }
}

interface PersistenceScope{
    fun Person.save()
    fun PersonId.load():Person
}

