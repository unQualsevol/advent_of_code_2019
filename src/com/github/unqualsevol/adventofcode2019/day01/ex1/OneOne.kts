import java.io.File

var fuel = 0
File("input").forEachLine {
    fuel += it.toInt() / 3 - 2
}
println(fuel)