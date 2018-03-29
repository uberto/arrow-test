package com.gamasoft.arrow.coroutine

import collatzConjecture
import collatzConjectureRec
import javafx.util.Duration
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.selects.select
import kotlinx.coroutines.experimental.selects.whileSelect
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.experimental.coroutineContext

internal class TasksProviderKtTest {

    @Test
    fun collatzConjectureTest() {
        assertEquals(2, collatzConjecture(2))
        assertEquals(3, collatzConjecture(4))
        assertEquals(4, collatzConjecture(8))
        assertEquals(5, collatzConjecture(16))
        assertEquals(8, collatzConjecture(3))  //3 10 5 16 8 4 2 1
    }


    @Test
    fun collatzConjectureRecTest() {
        assertEquals(2, collatzConjectureRec(2))
        assertEquals(3, collatzConjectureRec(4))
        assertEquals(4, collatzConjectureRec(8))
        assertEquals(5, collatzConjectureRec(16))
        assertEquals(8, collatzConjectureRec(3))  //3 10 5 16 8 4 2 1
    }

    @Test
    fun collatzConjecturePerformanceTest() {
        val s = System.nanoTime()
        var t = bigCollanz(123456790582135410)
        val elapsed = System.nanoTime() - s
        println("$elapsed          iteractions $t")
    }



    @Test
    fun collatzConjectureConcurrentTest() {

        val maxProduced = AtomicInteger(0)
        val maxConsumed = AtomicInteger(0)

        fun produceNumbers() = produce(capacity = 20) {
            var x = 123456790582135410
//            while (true) {

            while(!isClosedForSend) {

                send(x++) // produce next
                maxProduced.incrementAndGet()


            }

            println("finished!")

        }


        fun consumeNumbers(channel: ReceiveChannel<Long>) = launch {

            while (!channel.isClosedForReceive) {
                val x = channel.receive()

                val t = bigCollanz(x)
//                println("BigCollanz of $it is $t")
                maxConsumed.incrementAndGet()
            }
        }

        val producer = produceNumbers()

        val s = System.nanoTime()
        runBlocking {

             repeat(12) {
                 consumeNumbers(producer)
             }

            repeat(20) {
                delay(1000)
                println(it)
                println("produced ${maxProduced.get()}")
                println("consumed ${maxConsumed.get()}")
            }

            producer.cancel() // cancel producer coroutine and thus kill them all

            delay(100)
        }
        val elapsed = (System.nanoTime() - s) / 1_000_000_000.0

        println("$elapsed seconds ")
        println("produced ${maxProduced.get()}")
        println("consumed ${maxConsumed.get()}")
    }

    private fun bigCollanz(num: Long): Int {
        var t = 0
        repeat(1000) {
            t += collatzConjecture(num)
        }
        return t
    }



}

