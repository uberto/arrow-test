package com.gamasoft.arrow.monads

import arrow.core.Id
import arrow.core.fix
import arrow.core.value
import arrow.data.Reader
import arrow.data.ReaderFun
import arrow.data.fix
import arrow.data.runId
import arrow.instances.monad
import arrow.typeclasses.binding
import assertk.assert
import assertk.assertions.isEqualTo
import junit.framework.Assert.assertEquals
import org.junit.Test

internal class ReaderMonadTest {


    @Test
    fun readerFunctor() {
        val runF: ReaderFun<String, Context> = {dbConn -> Context(dbConn) }

        val reader: Reader<String, Context> = Reader(runF)

        val name = reader.run("myDbConn").fix()
                .map{it.getUser("Joe") }
                .map{it.name}
                .value()

        assertEquals("Joe", name)
    }

//    @Test
//    fun readerFunctorLift() {
//        val reader = Reader.applicative<String, Context>()
//
//        val name = reader.lift{ dbConn:String -> Id(Context(dbConn)) }
//                .run("myDbConn").fix()
//                .map{it.getUser("Joe") }
//                .map{it.name}
//                .value()
//
//        assertEquals("Joe", name)
//    }

    fun getUser(userId:String):Reader<Context, User> {
        val runF: ReaderFun<Context, User> = {ctx -> ctx.getUser(userId)}
        return Reader(runF)
    }

    fun getCommonFriends(u1: User, u2: User): Reader<Context, List<User>> =
            Reader{Id(it.findCommonFriends(u1, u2))}

    @Test
    fun readerMonad() {

        val user1 = "Frankie"
        val user2 = "Jonnny"

        val logic = Reader().monad<Context>().binding{
            val u1 = getUser(user1).bind()
            val u2 = getUser(user2).bind()

            getCommonFriends(u1, u2).bind()
        }.fix()
        //No Ctx specified yet (can be Db, RestApi, Mock etc.)

        val friends = logic.runId(Context("myDBServer"))
        //[User(name=Frankie_f), User(name=Jonnny_f)]

        assert(friends.toString()).isEqualTo(
                "[User(name=Frankie_f), User(name=Jonnny_f)]")

    }



}