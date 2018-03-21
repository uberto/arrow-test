package com.gamasoft.arrow.catamorphism

import junit.framework.Assert.assertEquals
import org.junit.Test

internal class CatamorphismTest{


    @Test
    fun FactorialRec(){
        var rec:(Long) ->Long = {42} //never used

        val fr = {x:Long -> if (x == 1L) x else x * rec(x - 1) }

        rec = fr

        assertEquals(2432902008176640000, fr(20))
    }

    @Test
    fun FactorialCata(){
        assertEquals(6, factorial(3))
        assertEquals(6, factorial2(3, ::factorial))
        assertEquals(6, factorial3(::factorial)(3))

     //   assertEquals(6, composeRec(::factorial3)(3))

//        val f:(Int) -> Int = composeRecGen(::factorial3lp)()
//        assertEquals(6, f(3) )

        assertEquals(6, cata(::fac)(3))
        assertEquals(120, cata(::fac)(5))
        assertEquals(3628800, cata(::fac)(10))

    }


    @Test
    fun FibonacciCata(){


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


}