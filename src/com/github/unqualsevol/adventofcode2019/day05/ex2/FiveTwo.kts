import java.io.File

val program = File("input").readText().split(",").toMutableList()

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

//println(program)
while (program[pos] != EOF) {
    val instruction = decode(program, pos)
    val jump = instruction.execute(program)
    if(jump == 0) {
        pos += instruction.length()
    } else {
        pos = jump
    }

//    println(program)
}

fun decode(program: MutableList<String>, index: Int): Instruction {
    val opCode = program[index]
    val instructionId = opCode.takeLast(2).toInt()
    var immediateA = false
    var immediateB = false
    var immediateC = false
    when {
        opCode.length >= 5 -> {
            immediateC = '1' == opCode.get(0)
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
            str(5, getOperatorValue(program, index + 1, true))
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
//            println("lt ${program[index]} ${program[index+1]} ${program[index+2]} ")
            lt(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB),
                    getOperatorValue(program, index + 3, true))
        }
        EQ_OPER -> {
//            println("eq ${program[index]} ${program[index+1]} ${program[index+2]} ")
            eq(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB),
                    getOperatorValue(program, index + 3, true))
        }
        else -> throw IllegalArgumentException("Invalid operator")
    }
}


fun getOperatorValue(program: List<String>, index: Int, immediate: Boolean): Int =
        if (immediate) program[index].toInt() else program[program[index].toInt()].toInt()

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

    override fun length(): Int = 2
}

class out(override val a: Int) : Instruction {
    override val b: Int
        get() = 0
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
        println(a)
        return 0
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