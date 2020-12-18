import java.io.File

var fuel = File("input").readLines().map { it.toInt() / 3 - 2 }.sum()
println("amount of fuel: $fuel")