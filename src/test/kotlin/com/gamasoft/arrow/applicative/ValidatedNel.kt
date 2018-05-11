package com.gamasoft.arrow.applicative

import arrow.data.*
import arrow.instances.semigroup

import assertk.assert
import assertk.assertions.*
import org.junit.Test

typealias Rule = (Int) -> ValidatedNel<BrokenRule, Int>

sealed class BrokenRule {
    override fun toString()= this.javaClass.simpleName

}
object MustBeBiggerThanZero : BrokenRule()
object MustBeEven : BrokenRule()
object MustBeSmallerThan10 : BrokenRule()


val positive: Rule = { n: Int ->
    if (n > 0) n.validNel() else MustBeBiggerThanZero.invalidNel()
}

val even: Rule = { n: Int ->
    if (n % 2 == 0) n.validNel() else MustBeEven.invalidNel()
}

fun smallerThan10(n:Int): ValidatedNel<BrokenRule, Int> =
    if (n < 10) n.validNel() else MustBeSmallerThan10.invalidNel()


fun ListK<Rule>.validate(n: Int): ValidatedNel<BrokenRule, Int> =
        foldLeft(ValidatedNel.validNel(n)) { acc, rule ->
            rule(n).combine(NonEmptyList.semigroup(), Int.semigroup(), acc)
        }
//
//fun ListK<Rule>.validate2(n: Int): ValidatedNel<BrokenRule, Int> =
//        map{it(n)}.combineAll() //java.lang.ClassNotFoundException: arrow.data.ValidatedMonoidInstanceImplicits

//Validated.monoid(IntMonoidInstance) { map{it(n)}.combineAll() }



//fun validate2(rules: ListKW<Rule>, n: Int): ValidatedNel<BrokenRule, Int> =
//        rules.ap({}, ValidatedNel.validNel(n))
//
//fun validate3(rules: ListKW<Rule>, n: Int): ValidatedNel<BrokenRule, Int> =
//        rules.traverse({},ValidatedNel.validNel(n))


fun <A> validatedMessage(value: A, validated: ValidatedNel<BrokenRule, A>): String =
        when(validated){
            is Valid -> "$value is valid! Hurray!"
            is Invalid -> "$value is not valid! ${validated.e.all}"
        }

internal class ValidatedNelExample {

    @Test
    fun validateRaul() {
        val n = 13
        val result = listOf(positive, even, ::smallerThan10).k().validate(n)
        val msg = validatedMessage(n, result)

        assert(result.isValid).isFalse()
        assert(result.isInvalid).isTrue()
        assert(result).isEqualTo(Invalid(NonEmptyList.of(MustBeSmallerThan10, MustBeEven)))
        assert(msg).isEqualTo("13 is not valid! [MustBeSmallerThan10, MustBeEven]")

    }


    @Test
    fun validateFold() {
        val n = 13
        val result = listOf(positive, even, ::smallerThan10).k().validate(n)
        val msg = validatedMessage(n, result)

        assert(result.isValid).isFalse()
        assert(result.isInvalid).isTrue()
        assert(result).isEqualTo(Invalid(NonEmptyList.of(MustBeSmallerThan10, MustBeEven)))
        assert(msg).isEqualTo("13 is not valid! [MustBeSmallerThan10, MustBeEven]")

    }


}