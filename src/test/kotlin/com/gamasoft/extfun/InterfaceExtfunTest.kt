package com.gamasoft.extfun

import org.junit.Test

internal class InterfaceExtfunTest{


    class TestPersistence: PersistenceScope{
        val people = mutableMapOf(1 to Person("Frank" , 1))

        override fun Int.load(): Person = people.get(this)!!

        override fun Person.save() { people.put(this.id, this) }

    }

    @Test
    fun simple(){

        val joe = Person("joe", 1234)

        TestPersistence().run{
            joe.save()
            val frank = 1.load()

            val j2 = 1234.load()

        } //return j2

        with(TestPersistence()){
            joe.save()
            val frank = 1.load()

            val j2 = 1234.load()
        }

        TestPersistence().apply{
            joe.save()
            val frank = 1.load()

            val j2 = 1234.load()
        } //return this




    }

}