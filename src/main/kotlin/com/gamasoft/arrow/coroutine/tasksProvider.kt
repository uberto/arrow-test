import arrow.core.Tuple2

fun collatzConjecture(start: Long): Int {
    var i = 1
    var x = start
    while (x > 1){
        i++
        x = if (x % 2 == 0L) x / 2 else x * 3 + 1
    }
    return i
}


fun collatzConjectureRec(start: Int): Int {
    val f = { x: Int -> if (x % 2 == 0) x / 2 else x * 3 + 1}

    var rec:(Pair<Int, Int>) ->Int = {42}

    val fr = {(x, i):Pair<Int, Int> -> if (x == 1) i else rec(f(x) to i+1) }

    rec = fr

    return fr(start to 1)
}


