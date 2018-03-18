package com.gamasoft.arrow.coroutine

import collatzConjecture
import javafx.util.Duration
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import org.junit.Test
import java.util.concurrent.TimeUnit

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
        val num = 123456790582135410
        val s = System.nanoTime()
        runBlocking<Unit> {
            withTimeout(10, TimeUnit.SECONDS) {



                val channel = Channel<Int>(4) // create buffered channel
                val sender = launch(coroutineContext) { // launch sender coroutine
                    repeat(10) {
                        println("Sending $it") // print before sending each element
                        channel.send(it) // will suspend when buffer is full
                    }
                }
                // don't receive anything... just wait....
                delay(1000)
                sender.cancel() // cancel sender coroutine


            }
        }
        val elapsed = System.nanoTime() - s
        println("$elapsed   ")
    }

    private fun bigCollanz(num: Long): Int {
        var t = 0
        repeat(1000) {
            t += collatzConjecture(num)
        }
        return t
    }



}

