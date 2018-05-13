package com.gamasoft.arrow.functors

import arrow.core.*
import arrow.data.valid
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import junit.framework.Assert.*

import org.junit.Test
import kotlin.reflect.KClass

internal class TryTest {


    @Test
    fun oldSchool() {

        val dollars = try {
            playLottery(9)
        } catch (e: AuthorizationException) {
            0
        }
        assertEquals(0, dollars)
    }

    @Test
    fun tryFailure() {
        val gain: Try<Int> = Try { playLottery(9) }

        assertTrue(gain.isFailure())

        when (gain) {
            is Failure -> assertEquals(AuthorizationException::class.java, gain.exception.javaClass)
            is Success -> fail()
        }
        assertEquals(0, gain.getOrDefault { 0 })

    }

    @Test
    fun tryFilter() {
        // If you want to perform a check on a possible success,
        // you can use filter to convert successful computations in failures if conditions aren’t met:
        val tryJackpot = Try { playLottery(10) }.filter { it > 500 }
        assertTrue(tryJackpot.isFailure())
    }

    @Test
    fun tryRecover() {
        val gain = Try { playLottery(99) }

        assertFalse(gain.isSuccess())

        assertEquals(Try.Success(1000),
                gain.recoverWith { Try { playLottery(42) } })
    }

    @Test
    fun tryFold() {
        // When you want to handle both cases of the computation you can use fold.
        // With fold we provide two functions,
        // one for transforming a failure into a new value,
        // the second one to transform the success value into a new one:
        val gain = Try { playLottery(99) }

        assertEquals(4,
                gain.fold({ 4 }, { error("not expected") }))

        val jackPot = Try { playLottery(42) }

        assertEquals(100_000,
                jackPot.fold({ error("not expected") }, { it * 100 }))
    }

    @Test
    fun tryFunctor() {

        // Transforming the value, if the computation is a success:
        val actual = Try { "3".toInt() }.map { it + 1 }
        assert(actual).isEqualTo(Try.Success(4))

        val actual2 = Try { "6".toInt() }
                                .map { it * 2 }
                                .map { it + 3 }
        assert(actual2.getOrDefault { 42 }).isEqualTo(15)

        val failure = Try { "nope".toInt() }.map { it + 1 }
        assert(failure.isFailure())

    }

    @Test
    fun tryLift() {
        val lifted = Try.functor().lift{x:String -> x.toInt()}

        assertEquals(Try.Success(6), lifted(Try.Success("6")))

    }

    @Test
    fun tryMonoid() {


//        Try.traverse().apply {
//            val actual = listOf<Try<Int>>(
//                    Success(1),
//                    Success(3),
//                    Failure(NullPointerException())).combineAll()
//
//            assertEquals(Try.Success(4), actual)
//            }

    }

    @Test
    fun tryApplicativeFailure() {

        val tryHarder = Try.applicative().tupled(
                Try { "3".toInt() },
                Try { "5".toInt() },
                Try { "nope".toInt() }
        ).fix()
        //Failure(exception=java.lang.NumberFormatException: For input string: "nope"

        assertEquals("Failure(exception=java.lang.NumberFormatException: For input string: \"nope\")", tryHarder.toString())
    }

    @Test
    fun tryApplicativeSuccess() {

        val tryHarder = Try.applicative().tupled(
                Try { "3".toInt() },
                Try { "5".toInt() },
                Try { "15".toInt() }
        )
        //Success(value=Tuple3(a=3, b=5, c=15)

        assertEquals("Success(value=Tuple3(a=3, b=5, c=15))", tryHarder.toString())
    }



    @Test
    fun tryNaturalTransformations() {
        val list =  Try { "3".toInt()}.toOption().toList()

        assertEquals("[3]", list.toString())

        val failure =  Try { "three".toInt()}.toOption().toList()

        assertEquals("[]", failure.toString())

    }

}


private open class GeneralException : Exception()

private object NoConnectionException : GeneralException()

private object AuthorizationException : GeneralException()

fun playLottery(guess: Int): Int {
    return when (guess) {
        42 -> 1000 // jackpot
        in 10..41 -> 1
        in 0..9 -> throw AuthorizationException
        else -> throw NoConnectionException
    }
}
