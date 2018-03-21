package com.gamasoft.arrow.catamorphism

/*

type Algebra f a = f a -> a -- the generic f-algebras

newtype Fix f = Iso { invIso :: f (Fix f) } -- gives us the initial algebra for the functor f

cata :: Functor f => Algebra f a -> (Fix f -> a) -- catamorphism from Fix f to a
cata alg = alg . fmap (cata alg) . invIso -- note that invIso and alg map in opposite directions


example
treeDepth :: Algebra (Tcon a) Integer -- again, the treeDepth f-algebra example
treeDepth (TconL x)   = 1
treeDepth (TconR y z) = 1 + max y z

cata treeDepth tree
*/

//typealias F<A> = (A) -> A
//typealias Algebra<A> = (F<A>) -> A
//
//data class Fix<T>(val invIso:F<Fix<F<*>>>)
//
//fun <A> cata(f:F<A>, x:A):A {
//    return f(cata(f, x))
//}



//--------

fun factorial(x: Int): Int = if (x == 1) 1 else x * factorial(x - 1)
//
//
//fun algeb(f: (Int)-> Int): (Int) -> Int = {x -> if (x == 1) 1 else x * f(x - 1)}
//
//
//fun fix(f:((Int) -> Int) -> Int): ((Int) -> Int) = {x -> f({y -> x})}
//
//fun factorialCata(x: Int):Int = algeb(fix({f -> algeb(f)}))(x) // wrong result
//





fun factorial2(x: Int, f:(Int) ->Int): Int = if (x == 1) 1 else x * f(x-1)



fun factorial3(f:(Int) ->Int): (Int) -> Int = {x -> factorial2(x, f)}

//------

typealias Fun<A> = (A) -> A


data class Fix<A>(val invIso: (Fix<A>) -> Fun<A>)


fun<A> cata(f:(Fun<A>) -> Fun<A>): Fun<A> = fixRecFunc(Fix { r -> f{ x:A -> fixRecFunc(r)(x) } })


fun <A> fixRecFunc(rec: Fix<A>):Fun<A> = rec.invIso(rec)


fun <A> yComp(f:(Fun<A>) -> Fun<A>): Fun<A> = cata(f)

//--






//fun cataInt(alg: Algebra<Int>): Fix<Int> =

//
//cata :: Functor f => Algebra f a -> (Fix f -> a) -- catamorphism from Fix f to a
//cata alg = alg . fmap (cata alg) . invIso -- note that invIso and alg map in opposite directions
//





typealias GFunc<T, R> = (T) -> R

data class RecursiveFunc<T, R>(val p: (RecursiveFunc<T, R>) -> GFunc<T, R>)

fun <T, R> y(f: (GFunc<T, R>) -> GFunc<T, R>): GFunc<T, R> {
    val rec = RecursiveFunc<T, R> { r -> f { r.p(r)(it) } }
    return rec.p(rec)
}

fun fac(f: GFunc<Int, Int>): GFunc<Int, Int> = { x: Int -> if (x <= 1) 1 else x * f(x - 1) }

fun fib(f: GFunc<Int, Int>) = { x: Int -> if (x <= 2) 1 else f(x - 1) + f(x - 2) }

fun main(args: Array<String>) {
    print("Factorial(1..10)   : ")
    for (i in 1..10) print("${y(::fac)(i)}  ")
    print("\nFibonacci(1..10)   : ")
    for (i in 1..10) print("${y(::fib)(i)}  ")
    println()
}