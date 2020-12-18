import java.io.File
import java.lang.IllegalArgumentException

val split = File("input").readText().split(",")
val program = IntArray(split.size) { split[it].trim().toInt() }
val SUM_OPER = 1
val MUL_OPER = 2
val EOF = 99

program[1] = 12
program[2] = 2

var pos = 0

while (program[pos] != EOF)
{
    val operator = program[pos]
    val first = program[pos+1]
    val second = program[pos+2]
    val destination = program[pos+3]

    program[destination] = when(operator)
    {
        SUM_OPER -> program[first]+program[second]
        MUL_OPER -> program[first]*program[second]
        else -> throw IllegalArgumentException("Invalid operator")
    }
    pos+=4
}

println(program[0])