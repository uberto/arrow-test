package com.gamasoft.arrow.applicative

import arrow.data.*
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.Assert
import org.junit.Test

typealias ValidationError = String
typealias EMail = String

typealias ValidationResult = Validated<ValidationError, EMail>
typealias ValidationFunction = (EMail) -> ValidationResult

internal class ValidatedExample {



    fun atMarkPresent(email: EMail):ValidationResult{
        val atMark = email.count { it == '@' }
        if (atMark == 1)
            return Validated.Valid(email)
        else
            return Validated.Invalid("There must be a @. There are $atMark in $email")
    }

    fun nameBefore(email: EMail):ValidationResult{
        val parts = email.split( '@' )
        if (parts.count() > 0 && parts[0].length >= 1)
            return Validated.Valid(email)
        else
            return Validated.Invalid("There must be a name before @ in $email")
    }

    fun domainAfter(email: EMail):ValidationResult{
        val parts = email.split( '@' )
        if (parts.count() > 1 && parts[1].length >= 1)
            return Validated.Valid(email)
        else
            return Validated.Invalid("There must be a domain after @ in $email")
    }

    @Test
    fun emailValidationRules() {

       val email = "fredATcompany"

        val validations = ListKW(listOf<ValidationFunction>(
                ::atMarkPresent, ::nameBefore, ::domainAfter))




        val vEmail = Validated.Valid(email)

        val v1 = vEmail.map(::atMarkPresent)
        println(v1)

    //    val v2 = vEmail.ap(validations)

        Assert.assertTrue(v1.isValid) //WRONG should be invalid
//
//        val vf1 = Validated.Valid(::atMarkPresent)
//        val v2 = vf1.ap(vf1).
//        println(v2)



//        Validated.applicative(validations).tupled()
//
//        validations.map(vEmail)
//
//                .map(::nameBefore).map(::doma)

//        val res2 = Validated.applicative<NonEmptyList<ValidationError>>().ap(vEmail, ::atMarkPresent)
//        val res3 = Validated.applicative<NonEmptyList<ValidationError>>().map(vEmail,  ::atMarkPresent).leftMap { ::nameBefore }.leftMap { ::domainAfter }
//        println(res3)

        val vEmail2 = Validated.Valid("@ddd")

//        val res4 = vEmail2.ap(::atMarkPresent )

//        val res4 = Validated.applicative<NonEmptyList<ValidationError>>().ap() map(vEmail2, ::atMarkPresent)
//        println(res4)

    }


    @Test
    fun validatedSequence() {

        val validations = NonEmptyList.of(
                ::atMarkPresent, ::nameBefore, ::domainAfter)


        fun validateEmail(email: EMail): ValidatedNel<ValidationError, EMail> {
            val validateds = validations.map { it(email) }
            val rValid = validateds.traverse({x->x.swap()},Validated.applicative()).ev().swap()
            return rValid
        }


        val result = validateEmail("ssd")
        println(result)


        assertk.assert(result.isValid).isFalse()
        assertk.assert(result.isInvalid).isTrue()
//        assertk.assert(result).isEqualTo(Invalid(NonEmptyList.of(MustBeSmallerThan10, MustBeEven)))
//        assertk.assert(msg).isEqualTo("13 is not valid! [MustBeSmallerThan10, MustBeEven]")

//        val rValid = validations.map { it("ub@gma.it") }.traverse({x -> x}, Validated.applicative() ).ev()

        val rValid = validateEmail("ub@gma.it")

        println(rValid)

//        val msg = validatedMessage(email, result)
//
        assertk.assert(rValid.isValid).isTrue()
        assertk.assert(rValid.isInvalid).isFalse()
//
    }
}

fun <A> validatedMessage(value: A, validated: ValidatedNel<ValidationError, A>): String =
        when(validated){
            is Valid -> "$value is valid! Hurray!"
            is Invalid -> "$value is not valid! ${validated.e.all}"
        }

//fun <E, A, B, C> parallelValidate
//        (v1: Validated<E, A>, v2: Validated<E, B>, f: (A, B) -> C): Validated<NonEmptyList<E>, C> {
//    return when {
//        v1 is Validated.Valid && v2 is Validated.Valid -> Validated.Valid(f(v1.a, v2.a))
//        v1 is Validated.Valid && v2 is Validated.Invalid -> v2.toValidatedNel()
//        v1 is Validated.Invalid && v2 is Validated.Valid -> v1.toValidatedNel()
//        v1 is Validated.Invalid && v2 is Validated.Invalid -> Validated.Invalid(NonEmptyList(v1.e, listOf(v2.e)))
//        else -> throw IllegalStateException("Not possible value")
//    }
//}



//fun domainAfter2(email: EMail): Either<EMail, ValidationError> {
//    val parts = email.split( '@' )
//    if (parts.count() > 1 && parts[1].length >= 1)
//        return Validated.Valid(email)
//    else
//        return Validated.Invalid("There must be a domain after @ in $email".nel())
//}




