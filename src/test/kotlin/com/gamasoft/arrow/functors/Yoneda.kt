package com.gamasoft.arrow.functors

import arrow.core.*
import arrow.data.*
import arrow.free.Yoneda
import arrow.instances.ListKFunctorInstance
import org.junit.Test


internal class YonedaTest {


    @Test
    fun yonedaLazyFunctorOption() {

        val yo = Yoneda(42.some(), Option.functor() )
        val ym = yo.map { it + 4 }. map { it / 2 }.map{it * 3}

        println("result " + ym.lower())

    }


    @Test
    fun yonedaLazyFunctorList() {

        val l = listOf(6, 42, 35, 67, 89, 90).k()

        val lazyFunctor = l.yoneda()
                .map { it + 4 }. map { it / 2 }.map{it * 3}

        //nothing happened yet...lazyFunctor
        val res = lazyFunctor.lower() //15,...

        println("result " + res)

    }


  //  fun <F, U> Functor<F>.yoneda(fa: Kind<F, U>) = Yoneda(fa , this )

    fun <T> Option<T>.functor() = Option.functor()

    fun <T> Option<T>.yoneda() = Yoneda(this , this.functor() )

    @Test
    fun yonedaExtFun() {

        8.some().functor()


        val lazyFunctor = 6.some().yoneda().map { it + 4 }. map { it / 2 }.map{it * 3}


        val res = lazyFunctor.lower() //15



        println("yo $res") //15

//        val res = Option.functor().yoneda(6.some()).map { it + 4 }. map { it / 2 }.map{it * 3}

    }


    private fun createListYoneda(fa: ListK<Int>): Yoneda<ForListK, Int> {
        val ff: ListKFunctorInstance = object : ListKFunctorInstance {}
        val yo = Yoneda(fa, ff)
        return yo
    }

    fun <T> ListK<T>.yoneda() = Yoneda(this , this.functor() )

    fun <T> ListK<T>.functor() = ListK.functor()

}