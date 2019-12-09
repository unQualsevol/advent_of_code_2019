import java.io.File

var combination = listOf(5, 6, 7, 8, 9)

val permutations = mutableSetOf(combination)

while (permutations.size < 120) {
    combination = combination.shuffled()
    permutations.add(combination)
}

var maxOutput = 0
var maxCombination = listOf<Int>()
val baseProgram = readInput()
permutations.forEach { permutation ->
    run {
        val (A, B, C, D, E) = permutation.map { Amplifier(baseProgram.toMutableList()) }
        A.runProgram(mutableListOf(permutation[0]))
        B.runProgram(mutableListOf(permutation[1]))
        C.runProgram(mutableListOf(permutation[2]))
        D.runProgram(mutableListOf(permutation[3]))
        E.runProgram(mutableListOf(permutation[4]))
        var parametersForA = mutableListOf(0)
        while (!A.finished && !B.finished && !C.finished && !D.finished && !E.finished){
            if(!A.finished) A.runProgram(parametersForA)
            if(!B.finished) B.runProgram(A.output)
            if(!C.finished) C.runProgram(B.output)
            if(!D.finished) D.runProgram(C.output)
            if(!E.finished) E.runProgram(D.output)
            parametersForA = E.output
        }
        println("permutation: $permutation output: ${E.output}")
        if(E.output[0] > maxOutput){
            maxOutput = E.output[0]
            maxCombination = permutation
        }
    }
}

println("maxValue: $maxOutput ids: $maxCombination")

class Amplifier(val program : MutableList<String>) {
    val output = mutableListOf<Int>()

    val EOF = "99"
    val SUM_OPER = 1
    val MUL_OPER = 2
    val STR_OPER = 3
    val OUT_OPER = 4
    val JPT_OPER = 5
    val JPF_OPER = 6
    val LT_OPER = 7
    val EQ_OPER = 8
    var pos = 0
    var finished = false;

    fun runProgram(parameters : MutableList<Int>) {
        while (program[pos] != EOF) {
            val instruction = decode(program, pos)
            val jump = when (instruction) {
                is str -> {
                    if(parameters.isEmpty())
                    {
                        return
                    }
                    val value = parameters[0]
                    parameters.removeAt(0)
                    instruction.executeWithValue(program, value)
                }
                is out -> {
                    output.add(instruction.execute(program))
                    0
                }
                else -> instruction.execute(program)
            }

            if (jump == 0) {
                pos += instruction.length()
            } else {
                pos = jump
            }
        }
        finished = true
    }

    fun decode(program: MutableList<String>, index: Int): Instruction {
        val opCode = program[index]
        val instructionId = opCode.takeLast(2).toInt()
        var immediateA = false
        var immediateB = false
        when {
            opCode.length >= 5 -> {
                immediateB = '1' == opCode.get(1)
                immediateA = '1' == opCode.get(2)
            }
            opCode.length == 4 -> {
                immediateB = '1' == opCode.get(0)
                immediateA = '1' == opCode.get(1)
            }
            opCode.length == 3 -> {
                immediateA = '1' == opCode.get(0)
            }
        }
        return when (instructionId) {
            SUM_OPER -> {
//            println("add ${program[index]} ${program[index+1]} ${program[index+2]} ${program[index+3]} ")
                add(getOperatorValue(program, index + 1, immediateA),
                        getOperatorValue(program, index + 2, immediateB),
                        getOperatorValue(program, index + 3, true))
            }
            MUL_OPER -> {
//            println("mul ${program[index]} ${program[index+1]} ${program[index+2]} ${program[index+3]} ")
                mul(getOperatorValue(program, index + 1, immediateA),
                        getOperatorValue(program, index + 2, immediateB),
                        getOperatorValue(program, index + 3, true))
            }
            STR_OPER -> {
//            println("str ${program[index]} ${program[index+1]} ")
                str(0, getOperatorValue(program, index + 1, true))
            }
            OUT_OPER -> {
//            println("out ${program[index]} ${program[index+1]} ")
                out(getOperatorValue(program, index + 1, immediateA))
            }
            JPT_OPER -> {
//            println("jpt ${program[index]} ${program[index+1]} ${program[index+2]} ")
                jpt(getOperatorValue(program, index + 1, immediateA),
                        getOperatorValue(program, index + 2, immediateB))
            }
            JPF_OPER -> {
//            println("jpf ${program[index]} ${program[index+1]} ${program[index+2]} ")
                jpf(getOperatorValue(program, index + 1, immediateA),
                        getOperatorValue(program, index + 2, immediateB))
            }
            LT_OPER -> {
//            println("lt ${program[index]} ${program[index+1]} ${program[index+2]} ${program[index+3]} ")
                lt(getOperatorValue(program, index + 1, immediateA),
                        getOperatorValue(program, index + 2, immediateB),
                        getOperatorValue(program, index + 3, true))
            }
            EQ_OPER -> {
//            println("eq ${program[index]} ${program[index+1]} ${program[index+2]} ${program[index+3]} ")
                eq(getOperatorValue(program, index + 1, immediateA),
                        getOperatorValue(program, index + 2, immediateB),
                        getOperatorValue(program, index + 3, true))
            }
            else -> {
                throw IllegalArgumentException("Invalid operator")}
        }
    }


    fun getOperatorValue(program: List<String>, index: Int, immediate: Boolean): Int =
            if (immediate) program[index].toInt() else program[program[index].toInt()].toInt()

}

interface Instruction {
    val a: Int
    val b: Int
    val rc: Int
    fun execute(program: MutableList<String>): Int
    fun length(): Int
}

class add(override val a: Int, override val b: Int, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
        program[rc] = (a + b).toString()
        return 0
    }

    override fun length(): Int = 4
}

class mul(override val a: Int, override val b: Int, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
        program[rc] = (a * b).toString()
        return 0
    }

    override fun length(): Int = 4
}

class str(override val a: Int, override val rc: Int) : Instruction {
    override val b: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
        program[rc] = a.toString()
        return 0
    }

    fun executeWithValue(program: MutableList<String>, value : Int): Int {
        program[rc] = value.toString()
        return 0
    }

    override fun length(): Int = 2
}

class out(override val a: Int) : Instruction {
    override val b: Int
        get() = 0
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
        println(a)
        return a
    }

    override fun length(): Int = 2
}

class jpt(override val a: Int, override val b: Int) : Instruction {
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
        if (a != 0) {
            return b
        }
        return 0
    }

    override fun length(): Int = 3
}

class jpf(override val a: Int, override val b: Int) : Instruction {
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
        if (a == 0) {
            return b
        }
        return 0
    }

    override fun length(): Int = 3
}

class lt(override val a: Int, override val b: Int, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
        program[rc] = if (a < b) "1" else "0"
        return 0
    }

    override fun length(): Int = 4
}

class eq(override val a: Int, override val b: Int, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
        program[rc] = if (a == b) "1" else "0"
        return 0
    }

    override fun length(): Int = 4
}

fun readInput() = File("input").readText().split(",")