package com.gamasoft.arrow.monads

import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test


internal class CandyTest{

    @Test
    fun randoms(){

        val xs = randoms(2).take(4).toList()

        println(xs)

        Assert.assertEquals(xs.size, 4)
    }

    @Test
    fun toCandy(){

        val c = toCandy(6)

        Assert.assertEquals(Candy.Green, c)
    }
   @Test
    fun candies(){

        val cs = candies(6).take(5).toList()


        println(cs)
        Assert.assertEquals(5, cs.size)
    }

    @Test
    fun processInputsTest(){
        val inputs = listOf(Input.Coin, Input.Turn,Input.Coin, Input.Turn)
        val rcd = CandyDispenser(DispenserState.Locked(10), 12347, null)
        val res = processInputs(rcd, inputs)

        assertEquals(res.first.state, DispenserState.Locked(8))
        assertEquals(res.second, listOf(Candy.Red, Candy.Black))
    }
}