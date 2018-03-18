package com.gamasoft.arrow.monoids

import arrow.typeclasses.monoid
import org.junit.Test
import arrow.syntax.monoid.*
import arrow.core.*
import arrow.syntax.semigroup.combine
import junit.framework.Assert.assertEquals
import org.junit.Assert


/*** Arrow.io documentation as runnable code ***/
internal class MonoidTest {

    @Test
    fun emptyLaw() {
        val StringMonoid = monoid<String>()

        val empty = StringMonoid.empty()

        assertEquals("a", empty.combine("a"))
        assertEquals("a", StringMonoid.combine("a", empty))
    }

    @Test
    fun associativityLaw() {

        assertEquals(12, 3.combine( 4.combine(5) ))
        assertEquals(12, (3.combine(4)).combine(5))
    }

    @Test
    fun combineAll() {

        val word = listOf("Λ", "R", "R", "O", "W").combineAll()

        assertEquals("ΛRROW", word)
    }
}