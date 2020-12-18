import java.io.File

val input = File("input").readText().split(",")
val elements = Array(10000000, { "0" }).toList()
val program = (input + elements).toMutableList()

val debug = false
val EOF = "99"

val SUM_OPER = 1
val MUL_OPER = 2
val STR_OPER = 3
val OUT_OPER = 4
val JPT_OPER = 5
val JPF_OPER = 6
val LT_OPER = 7
val EQ_OPER = 8
val ARB_OPER = 9

var relativeBase = 0

var pos = 0

//println(program)
while (program[pos] != EOF) {
    val instruction = decode(program, pos)
    val jump = instruction.execute(program)
    if (jump == -1) {
        pos += instruction.length()
    } else {
        pos = jump
    }

    if (debug) println("pos: $pos")
}

fun decode(program: MutableList<String>, index: Int): Instruction {
    val opCode = program[index]
    val instructionId = opCode.takeLast(2).toInt()
    var immediateA = '0'
    var immediateB = '0'
    var immediateC = '0'
    when {
        opCode.length >= 5 -> {
            immediateC = opCode[0]
            immediateB = opCode[1]
            immediateA = opCode[2]
        }
        opCode.length == 4 -> {
            immediateB = opCode[0]
            immediateA = opCode[1]
        }
        opCode.length == 3 -> {
            immediateA = opCode[0]
        }
    }
    return when (instructionId) {
        SUM_OPER -> {
            if (debug) println("add ${program[index]} ${program[index + 1]} ${program[index + 2]} ${program[index + 3]} ")
            add(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB),
                    getOperatorWriteValue(program, index + 3, immediateC))
        }
        MUL_OPER -> {
            if (debug) println("mul ${program[index]} ${program[index + 1]} ${program[index + 2]} ${program[index + 3]} ")
            mul(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB),
                    getOperatorWriteValue(program, index + 3, immediateC))
        }
        STR_OPER -> {
            if (debug) println("str ${program[index]} ${program[index + 1]} ")
            str(1L, getOperatorWriteValue(program, index + 1, immediateA))
        }
        OUT_OPER -> {
            if (debug) println("out ${program[index]} ${program[index + 1]} ")
            out(getOperatorValue(program, index + 1, immediateA))
        }
        JPT_OPER -> {
            if (debug) println("jpt ${program[index]} ${program[index + 1]} ${program[index + 2]} ")
            jpt(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB))
        }
        JPF_OPER -> {
            if (debug) println("jpf ${program[index]} ${program[index + 1]} ${program[index + 2]} ")
            jpf(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB))
        }
        LT_OPER -> {
            if (debug) println("lt ${program[index]} ${program[index + 1]} ${program[index + 2]} ${program[index + 3]} ")
            lt(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB),
                    getOperatorWriteValue(program, index + 3, immediateC))
        }
        EQ_OPER -> {
            if (debug) println("eq ${program[index]} ${program[index + 1]} ${program[index + 2]} ${program[index + 3]} ")
            eq(getOperatorValue(program, index + 1, immediateA),
                    getOperatorValue(program, index + 2, immediateB),
                    getOperatorWriteValue(program, index + 3, immediateC))
        }
        ARB_OPER -> {
            if (debug) print("abr ${program[index]} ${program[index + 1]} previous: $relativeBase ")
            relativeBase += getOperatorValue(program, index + 1, immediateA).toInt()
            if (debug) println("new: $relativeBase")
            abr()
        }
        else -> throw IllegalArgumentException("Invalid operator")
    }
}


fun getOperatorValue(program: List<String>, index: Int, immediate: Char): Long =
        when (immediate) {
            '1' -> program[index].toLong()
            '2' -> program[program[index].toInt() + relativeBase].toLong()
            else -> program[program[index].toInt()].toLong()
        }

fun getOperatorWriteValue(program: List<String>, index: Int, immediate: Char): Int =
        when (immediate) {
            '2' -> program[index].toInt() + relativeBase
            else -> program[index].toInt()
        }

interface Instruction {
    val a: Long
    val b: Long
    val rc: Int
    fun execute(program: MutableList<String>): Int
    fun length(): Int
}

class add(override val a: Long, override val b: Long, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        program[rc] = (a + b).toString()
        return -1
    }

    override fun length(): Int = 4
}

class mul(override val a: Long, override val b: Long, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        program[rc] = (a * b).toString()
        return -1
    }

    override fun length(): Int = 4
}

class str(override val a: Long, override val rc: Int) : Instruction {
    override val b: Long
        get() = 0

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        program[rc] = a.toString()
        return -1
    }

    override fun length(): Int = 2
}

class out(override val a: Long) : Instruction {
    override val b: Long
        get() = 0
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        println(a)
        return -1
    }

    override fun length(): Int = 2
}

class jpt(override val a: Long, override val b: Long) : Instruction {
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        if (a != 0L) {
            return b.toInt()
        }
        return -1
    }

    override fun length(): Int = 3
}

class jpf(override val a: Long, override val b: Long) : Instruction {
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        if (a == 0L) {
            return b.toInt()
        }
        return -1
    }

    override fun length(): Int = 3
}

class lt(override val a: Long, override val b: Long, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        program[rc] = if (a < b) "1" else "0"
        return -1
    }

    override fun length(): Int = 4
}

class eq(override val a: Long, override val b: Long, override val rc: Int) : Instruction {

    override fun execute(program: MutableList<String>): Int {
//        println("a: $a b: $b rc: $rc")
        program[rc] = if (a == b) "1" else "0"
        return -1
    }

    override fun length(): Int = 4
}

class abr() : Instruction {
    override val a: Long
        get() = 0
    override val b: Long
        get() = 0
    override val rc: Int
        get() = 0

    override fun execute(program: MutableList<String>): Int = -1

    override fun length(): Int = 2
}