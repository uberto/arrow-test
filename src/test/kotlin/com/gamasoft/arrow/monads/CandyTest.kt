package com.gamasoft.arrow.monads

import org.junit.Assert
import org.junit.Test


internal class CandyTest{

    @Test
    fun randoms(){

        val xs = randoms(2).take(4).toList()

        println(xs)

        Assert.assertEquals(xs.size, 4)
    }
}