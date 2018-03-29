package com.gamasoft.arrow.catamorphism

import junit.framework.Assert.assertEquals
import org.junit.Test

internal class CatamorphismTest{


    @Test
    fun factorialRec(){
        var rec:(Long) ->Long = {42} //never used

        val fr = {x:Long -> if (x == 1L) x else x * rec(x - 1) }

        rec = fr

        assertEquals(2432902008176640000, fr(20))
    }

    @Test
    fun factorialCata(){
        assertEquals(6, factorial(3))
        assertEquals(6, factorial2(3, ::factorial))
        assertEquals(6, factorial3(::factorial)(3))
//        assertEquals(6, factorialCata(3))  //SO


        assertEquals(6, cata(::fac)(3))
        assertEquals(120, cata(::fac)(5))
        assertEquals(3628800, cata(::fac)(10))

    }


    @Test
    fun fibonacciCata(){


        assertEquals(1, cata(::fib)(1))
        assertEquals(1, cata(::fib)(2))
        assertEquals(2, cata(::fib)(3))
        assertEquals(3, cata(::fib)(4))
        assertEquals(5, cata(::fib)(5))
        assertEquals(8, cata(::fib)(6))
        assertEquals(13, cata(::fib)(7))
        assertEquals(21, cata(::fib)(8))
        assertEquals(34, cata(::fib)(9))

    }




    @Test
    fun cataBuild1(){

        val r =fac(fac(fac{x -> 1}))
        assertEquals(24, r(4))
        assertEquals(720, r(10)) //wrong  10*9*8

    }

    data class Box<A>(val extract: (Box<A>) -> Fun<A>)

    @Test
    fun cataBuild2(){

        val b1 = Box<String>(extract = {b -> b.extract(b)})
        val b2 = Box<String>(extract = { b -> {x -> "$x 42"}})

        assertEquals("a 42", b1.extract(b2)("a") ) //wrong  10*9*8

    }


    @Test
    fun cataBuild3(){

        val b3 = Box<Int>(extract = { b -> {fac(b.extract(b))(it) }})

        assertEquals(6, b3.extract(b3)(3) ) //correct

    }


    @Test
    fun cataBuild4(){

        //        fun f0(b:Box<Int>, x:Int) = b.extract(b)(x)   //stack overflow
        fun f1(b:Box<Int>, x:Int) = fac( b.extract(b) )(x)

        val f2 = {b:Box<Int> -> {x:Int -> f1(b, x)}}

        val box = Box(extract = f2)

        assertEquals(6, box.extract(box)(3) ) //correct
        assertEquals(3628800, box.extract(box)(10) ) //correct


        //compact
        val b1  =Box(extract = {b:Box<Int> -> {fac( b.extract(b) )(it)}})

        assertEquals(6, b1.extract(b1)(3) ) //correct

        //fixed point of a function is the input point that produce same output: if f=x*x fixed points are 0 and 1
        //fixed point of a functor is the function that produce same function:
        //fixed point of a functor is the initial algebra
    }

}