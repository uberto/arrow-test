package com.gamasoft.extfun

data class Person(val name: String, val id: Int)

interface PersistenceScope{
    fun Person.save()

    fun Int.load():Person
}

