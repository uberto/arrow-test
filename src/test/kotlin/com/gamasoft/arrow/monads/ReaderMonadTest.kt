package com.gamasoft.arrow.monads

import arrow.core.value
import arrow.data.Reader
import arrow.data.applicative
import arrow.data.ev
import arrow.syntax.functor.map
import arrow.typeclasses.binding
import junit.framework.Assert.assertEquals
import org.junit.Test

internal class ReaderMonadTest {


    @Test
    fun readerFunctor() {
        val reader = Reader{dbConn:String -> Context(dbConn)}

        val name = reader.run("myDbConn")
                .map{it.getUser("Joe") }
                .map{it.name}
                .value()

        assertEquals("Joe", name)
    }

//    @Test
//    fun readerFunctorLift() {
//        val reader = Reader.applicative<String, Context>()
//
//        val name = reader.lift{ dbConn:String -> Context(dbConn) }
//                .run("myDbConn")
//                .map{it.getUser("Joe") }
//                .map{it.name}
//                .value()
//
//        assertEquals("Joe", name)
//    }

    fun getUserFromContext(userId:String) = Reader<Context, User>{ctx -> ctx.getUser(userId)}

    @Test
    fun readerMonad() {

        val res = Reader().monad<Context>().binding{

            val u7 = getUserFromContext("Frankie").bind()
            val u6 = getUserFromContext("Jonnny").bind()

           "$u7 & $u6"
        }.ev().run(Context("pippo"))


        assertEquals("User(name=Frankie) & User(name=Jonnny)", res.value())

    }



}