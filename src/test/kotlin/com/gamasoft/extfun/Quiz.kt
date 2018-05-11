package com.gamasoft.extfun

import org.junit.Test

internal class QuizTest() {

    fun hello() = {
        println("Hello, World")
    }

    @Test
    fun functionBrakets() {

        hello()
        hello()()

    }

    @Test
    fun indentTrimming() {

        val world = "multiline world"
        println("""
    Hello
    \$world
""".trimIndent())



    }

    fun printNumberSign(num: Int) {
        if (num < 0) {
            "negative"
        } else if (num > 0) {
            "positive"
        } else {
            "zero"
        }.let { print(it) }
    }


    @Test
    fun nestedIf() {

        printNumberSign(-2)
        print(",")
        printNumberSign(0)
        print(",")
        printNumberSign(2)
    }
}