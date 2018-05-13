package com.gamasoft.delegation

import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Test

//https://kotlinlang.org/docs/reference/delegation.html

interface Printer {
    fun print(msg: String)
}

class PrinterStdOut() : Printer {
    override fun print(msg: String) = println(msg)
}

interface Scanner {
    fun scan(): String
}

class ScannerConst(val c: String) : Scanner {
    override fun scan(): String = c
}

class AlsoPrinter(p: Printer) : Printer by p {
    fun otherMethod(){
        ///do something else
    }
}


class MultiFunction(p: Printer, s: Scanner) : Printer by p, Scanner by s

internal class DelegationTest {

    @Test
    fun delegateInheritance() {
        val ap = AlsoPrinter(PrinterStdOut())
        ap.print("hello")
        assert(ap is Printer)
    }



    @Test
    fun multipleInheritance() {
        val mf = MultiFunction(PrinterStdOut(), ScannerConst("hello"))
        val msg = mf.scan()
        assert( msg).isEqualTo("hello")
        mf.print(msg)
        assert(mf is Scanner)
        assert(mf is Printer)
    }
}

