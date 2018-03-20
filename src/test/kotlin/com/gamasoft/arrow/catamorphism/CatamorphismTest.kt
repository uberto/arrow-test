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
        assertEquals(1, factorialCata(1))
        assertEquals(2, factorialCata(2))
        assertEquals(6, factorialCata(3))
        assertEquals(24, factorialCata(4))
        assertEquals(120, factorialCata(5))
        assertEquals(2432902008176640000, factorialCata(20))
    }


    @Test
    fun FactorialRect(){

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


}