package com.gamasoft.extfun

import org.junit.Test

internal class InterfaceExtfunTest{


    object TestPersistence: PersistenceScope{
        val people = mutableMapOf(1 to Person("Frank" , 1))

        override fun Int.load(): Person = people.get(this)!!

        override fun Person.save() { people.put(this.id, this) }
    }

    @Test
    fun simple(){

        val joe = Person("joe", 1234)

        val ff = Person(name = "ff", id = 234)

        val frank = loadFriend(joe)(TestPersistence)

        with(TestPersistence){
            joe.save()
            val frank = 1.load()

            val j2 = 1234.load()

        }

        TestPersistence.apply{
            joe.save()
            val frank = 1.load()

            val j2 = 1234.load()
        } //return this



        TestPersistence.run{
            joe.save()
            val frank = 1.load()

            frank.beFriend(joe)
            frank
        }


    }



    private fun loadFriend(joe: Person): (PersistenceScope) -> Person {
        return {x:PersistenceScope ->
            x.run {
                joe.save()
                val frank = 1.load()

                frank.beFriend(joe)
                frank
            } //return frank
        }
    }

}