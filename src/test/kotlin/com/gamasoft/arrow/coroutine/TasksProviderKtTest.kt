package com.gamasoft.arrow.coroutine

import collatzConjecture
import javafx.util.Duration
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
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

        fun produceNumbers() = produce(capacity = 10) {
            var x = 123456790582135410
//            while (true) {

            for(i in 1 ..100) {

                val t = System.currentTimeMillis()
                while (System.currentTimeMillis() - t < 1_000) {


                    //                println("Sending $x") // print before sending each element
                    send(x++) // produce next
                    maxProduced.incrementAndGet()
                }
                println(i)
            }

            println("finished!")
        }


        fun consumeNumbers(channel: ReceiveChannel<Long>) = launch {

            while (true) {
                val x = channel.receive()

                val t = bigCollanz(x)
//                println("BigCollanz of $it is $t")
                maxConsumed.incrementAndGet()
            }
        }

        val producer = produceNumbers()

        val s = System.nanoTime()
        runBlocking {

//            for(i in 1 ..100) {
//                val t = System.currentTimeMillis()
//                while (System.currentTimeMillis() - t < 1_000) {
//                    consumeNumbers(producer)
//                }
//                println(i)
//            }
//            while(!producer.isClosedForReceive)
             repeat(10) {
                 consumeNumbers(producer)
             }
//


//            producer.cancel() // cancel producer coroutine and thus kill them all
//            delay(1000)

            while (!producer.isClosedForReceive)
                delay(100)
        }
        val elapsed = (System.nanoTime() - s) / 1_000_000_000.0

        println("$elapsed seconds ")
        println(maxProduced.get())
        println(maxConsumed.get())
    }

    private fun bigCollanz(num: Long): Int {
        var t = 0
        repeat(1000) {
            t += collatzConjecture(num)
        }
        return t
    }



}

