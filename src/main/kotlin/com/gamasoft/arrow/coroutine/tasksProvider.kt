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
    val f = { (x, i):Tuple2<Int, Int> -> (if (x % 2 == 0) x / 2 else x * 3 + 1) to i+1}

    return 42
}