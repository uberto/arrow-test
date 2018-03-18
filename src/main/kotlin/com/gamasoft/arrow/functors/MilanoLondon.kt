package com.gamasoft.arrow.functors

import arrow.HK
import arrow.higherkind
import arrow.instances.StringSemigroupInstance
import arrow.typeclasses.Functor
import arrow.typeclasses.Monoid


//public inline fun <A> arrow.data.TryKind<A> /* = arrow.HK<arrow.data.TryHK, A> */.ev(): arrow.data.Try<A> { /* compiled code */ }

public typealias LondonKind<A>  = arrow.HK<LondonHK, A>


data class PalazzoReale(val name: String)

data class Duomo(val  name: String)
data class Castello(val name: String)

//
//
////

@higherkind
sealed class London<out A> : LondonKind<A> {

    companion object {

    }
}
//
//
//
//}
//
class LondonHK(){}


//class LondonF<City>: Functor<City> {
//    override fun <A, B> map(fa: City<A>, f: (A) -> B): City<B> {
//    }
//}

//interface Functor<F>{
//    fun <A,B> fmap(fa: F<A>, f: (A)->B): F<B>
//}

//
//object StringMonoidInstance : Monoid<String> {
//    override fun empty(): String = ""
//
//    override fun combine(a: String, b: String): String = StringSemigroupInstance.combine(a, b)
//}
//
//object StringMonoidInstanceImplicits {
//
//    fun instance(): StringMonoidInstance = StringMonoidInstance
//}