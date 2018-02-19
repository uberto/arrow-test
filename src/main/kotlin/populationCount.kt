fun population_count(number: Int, tot: Int = 0): Int =
        if (number == 0)
            tot
        else
            population_count(number / 2, tot + if (number % 2 == 0) 0 else 1)



fun main(args : Array<String>){
    println("population_count(0) = ${population_count(0)}")
    println("population_count(5) = ${population_count(5)}")
    println("population_count(8) = ${population_count(8)}")
    println("population_count(15) = ${population_count(15)}")
    println("population_count(19) = ${population_count(19)}")
}