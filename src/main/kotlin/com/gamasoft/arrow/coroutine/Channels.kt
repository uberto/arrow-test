package com.gamasoft.arrow.coroutine

import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.*
import java.util.concurrent.ThreadLocalRandom as Rnd

suspend fun foo(channel1: Channel<Int>, channel2: Channel<Int>, done: Channel<Boolean>) {
    val temp1 = Rnd.current().nextInt(100)
    val int1 = channel1.receive()
    val temp2 = Rnd.current().nextInt(100)
    val int2 = channel2.receive()
    println("value: ${temp1} + ${int1} + ${temp2} + ${int2}")
    done.send(true)
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val done = Channel<Boolean>()
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    launch(context) { foo(channel1, channel2, done) }
    val x1 = Rnd.current().nextInt(100)
    channel1.send(x1)
    println("sent $x1")
    val x2 = Rnd.current().nextInt(100)
    channel2.send(x2)
    println("sent $x2")
    done.receive()
}