package com.gamasoft.arrow.applicative

import arrow.data.*
import org.junit.Test

typealias Error = String
typealias EMail = String

typealias ValidationResult = Validated<NonEmptyList<Error>, EMail>
typealias ValidationFunction = (EMail) -> ValidationResult

internal class ValidatedExample {



    fun atMarkPresent(email: EMail):ValidationResult{
        val atMark = email.count { it == '@' }
        if (atMark == 1)
            return Validated.Valid(email)
        else
            return Validated.Invalid("There must be a @. There are $atMark in $email".nel())
    }

    fun nameBefore(email: EMail):ValidationResult{
        val parts = email.split( '@' )
        if (parts.count() > 0 && parts[0].length >= 1)
            return Validated.Valid(email)
        else
            return Validated.Invalid("There must be a name before @ in $email".nel())
    }

    fun domainAfter(email: EMail):ValidationResult{
        val parts = email.split( '@' )
        if (parts.count() > 1 && parts[1].length >= 1)
            return Validated.Valid(email)
        else
            return Validated.Invalid("There must be a domain after @ in $email".nel())
    }

    @Test
    fun emailValidationRules() {

       val email: EMail = "fredATcompany"

        val validations = listOf<ValidationFunction>(
                ::atMarkPresent, ::nameBefore, ::domainAfter)

        val vEmail = Validated.Valid(email)

//        val res2 = Validated.applicative<NonEmptyList<Error>>().ap(vEmail, ::atMarkPresent)
//        val res3 = Validated.applicative<NonEmptyList<Error>>().map(vEmail,  ::atMarkPresent).leftMap { ::nameBefore }.leftMap { ::domainAfter }
//        println(res3)

        val vEmail2 = Validated.Valid("@ddd")

//        val res4 = vEmail2.ap(::atMarkPresent )

//        val res4 = Validated.applicative<NonEmptyList<Error>>().ap() map(vEmail2, ::atMarkPresent)
//        println(res4)

    }
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

