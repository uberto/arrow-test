package com.gamasoft.arrow.applicative

import arrow.core.Tuple2
import arrow.data.ListK
import arrow.data.applicative
import arrow.data.fix
import arrow.data.k
import org.junit.Test

class ListApplicativeTest {

    @Test
    fun simple(){
        val vals = listOf(1, 2, 3).k()

        val plusOne: (Int) -> Int = { it + 1}
        val doubleIt: (Int) -> Int = { it * 2}
        val funs = listOf(plusOne, doubleIt).k()

        val res = vals.ap(funs)
        //[2, 3, 4, 2, 4, 6]

        println(res)
    }


    @Test
    fun map2(){

        val vals1 = listOf(1, 2, 3).k()
        val vals2 = listOf(4, 5).k()
        val sum: (Tuple2<Int, Int>) -> Int = {it.a + it.b }

        val res = vals1.map2(vals2, sum)

        val res2 = ListK.applicative().tupled(vals1, vals2).fix().map(sum)
        //[2, 3, 4, 2, 4, 6]

        println(res)
        println(res2)

    }

}