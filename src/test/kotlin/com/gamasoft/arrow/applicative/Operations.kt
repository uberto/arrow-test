package com.gamasoft.arrow.applicative

import arrow.core.*
import arrow.data.*
import arrow.data.ListK.Companion.empty
import arrow.data.ListK.Companion.just

import arrow.instances.*
import arrow.typeclasses.monoid
import org.junit.Test

typealias IntValidation = (Int) -> ValidatedNel<String, Int>




typealias UValidated<E, A> = Validated<ListK<E>, A>
typealias UValid<A> = Validated.Valid<A>
typealias UInvalid<E> = Validated.Invalid<ListK<E>>

internal class OperationsExample {

@Test
fun applicativeSum(){

    val v1 = ListK.just(3)
    val funs = listOf({ x: Int -> x + 3 }, { x: Int -> x * 2 }, { x: Int -> x - 1 })

    val fs = ListK(funs)

    val r = v1.ap(fs).list

    println(r)

    }


@Test
fun applicativeValidation() {

    val positive = { x: Int -> if (x > 0) Right(x) else Left("must be bigger than 0") }
    val even = { x: Int -> if (x % 2 == 0) Right(x) else Left("must be even") }
    val smallerThan10 = { x: Int -> if (x < 10) Right(x) else Left("must be smaller than 10") }


    val fs = listOf(positive, even, smallerThan10).k()

    val v1 = ListK.just(13)
    val errors = v1.ap(fs).flatMap{ x -> if (x is Either.Left) just(x.a) else empty() }.list

    println(errors)


}

//
//    fun Int.validateAgainst(rules: ListK<IntValidation>): ValidatedNel<String, Int> = // rules.map { it(this) }.fold()
//     rules.map { it(this) }.combineAll()
//
//
//    @Test
//    fun applicativeValidated() {
//
//        val positive = { x: Int -> if (x > 0) Valid(x) else Invalid("must be bigger than 0".nel()) }
//        val even = { x: Int -> if (x % 2 == 0) Valid(x) else Invalid("must be even".nel()) }
//        val smallerThan10 = { x: Int -> if (x < 10) Valid(x) else Invalid("must be smaller than 10".nel()) }
//
//
//
//        val fs = listOf(positive, even, smallerThan10).k()
//
//        val r = 13.validateAgainst(fs)
//
//
//        println(r)
//
////        nel().ap(fs)
////
////        println(fs.ap()(13))
//
//
//
//        val v1:Validated<String, Int> = 13.valid()
//
////        val dsds = v1.combine(fs)
////        val errors = v1. toValidatedNel().ap(fs).flatMap{ x -> if (x is Invalid<*>) pure(x.a) else empty() }.list
//
////        println(errors)
//
//
//
//    }

}