package com.gamasoft.arrow.coroutine

import collatzConjecture
import collatzConjectureRec
import javafx.util.Duration
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*
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
    fun collatzConjectureConcurrentProduceConsumeTest() {

        val maxProduced = AtomicInteger(0)
        val maxConsumed = AtomicInteger(0)

        fun produceNumbers() = produce(capacity = 20) {
            var x = 123456790582135410
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

    @Test
    fun collatzConjectureConcurrentActorTest() {

        val maxProduced = AtomicInteger(0)
        val maxConsumed = AtomicInteger(0)

        val workerChannels = Array(10){buildNewWorker("worker $it")}

        fun produceNumbers() = actor<CollantzMsg>() {

//            for (msg in channel)
//                when(msg){
//                    is CollantzMsg.Start <- startProducing
//                }

               var x = 123456790582135410

               for(wc in workerChannels){ //use select
                   if (!wc.isFull)
                       wc.send(CollantzMsg.Calc(x++))
               }


            println("finished!")

        }


        fun consumeNumbers(producerActor: SendChannel<CollantzMsg>) = actor<CollantzMsg> {



            //TODO listen to the actor
            while (!channel.isClosedForReceive) {

                val x = channel.receive()

                when(x){
                    is CollantzMsg.Calc ->{

                        val t = bigCollanz(x.number)
//                println("BigCollanz of $it is $t")
                        maxConsumed.incrementAndGet()

                    }
                }
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

            producer.close() // cancel producer coroutine and thus kill them all

            delay(100)
        }
        val elapsed = (System.nanoTime() - s) / 1_000_000_000.0

        println("$elapsed seconds ")
        println("produced ${maxProduced.get()}")
        println("consumed ${maxConsumed.get()}")
    }

    private fun buildNewWorker(id: String) = actor<CollantzMsg> {

    }


    private fun bigCollanz(num: Long): Int {
        var t = 0
        repeat(1000) {
            t += collatzConjecture(num)
        }
        return t
    }



}

sealed class CollantzMsg {

    class Calc(val number:Long): CollantzMsg()

//    class Start(actorsChanells: Array<SendChannel<CollantzMsg>>):CollantzMsg()
//
//    object Stop: CollantzMsg()

}

