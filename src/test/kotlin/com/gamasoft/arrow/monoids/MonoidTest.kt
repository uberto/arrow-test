package com.gamasoft.arrow.monoids

import org.junit.Test
import arrow.instances.monoid
import assertk.assert
import assertk.assertions.isEqualTo


/*** Arrow.io documentation as runnable code ***/
internal class MonoidTest {

    @Test
    fun emptyLaw() {
        String.monoid().run {
            assert(empty().combine("a")).isEqualTo("a")
            assert("a".combine( empty())).isEqualTo("a")
        }
    }

    @Test
    fun associativityLaw() {
        Int.monoid().apply {
            assert(3.combine(4.combine(5))).isEqualTo(12)
            assert((3.combine(4)).combine(5)).isEqualTo(12)
        }
    }

    @Test
    fun combineAll() {
        String.monoid().apply {
            val word = listOf("Λ", "R", "R", "O", "W").combineAll()
            assert(word).isEqualTo("ΛRROW")
        }
    }
}