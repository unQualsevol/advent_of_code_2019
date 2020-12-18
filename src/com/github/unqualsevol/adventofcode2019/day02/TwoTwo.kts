import java.io.File
import java.lang.IllegalArgumentException

val EXPECTED_VALUE = 19690720
val SUM_OPER = 1
val MUL_OPER = 2
val EOF = 99

 outer@ for (noun in 0..99) {
    for (verb in 0..99) {
        val split = File("input").readText().split(",")
        val program = IntArray(split.size) { split[it].trim().toInt() }


        program[1] = noun
        program[2] = verb

        var pos = 0

        while (program[pos] != EOF) {
            val operator = program[pos]
            val first = program[pos + 1]
            val second = program[pos + 2]
            val destination = program[pos + 3]

            program[destination] = when (operator) {
                SUM_OPER -> program[first] + program[second]
                MUL_OPER -> program[first] * program[second]
                else -> throw IllegalArgumentException("Invalid operator")
            }
            pos += 4
        }

        if (program[0] == EXPECTED_VALUE) {
            println("noun $noun verb $verb result ${noun*100+verb}")
            break@outer
        }
    }
}