import java.io.File

var fuel = 0
var fuelForFuel = 0
File("input").forEachLine {
    val moduleFuel = it.toInt() / 3 - 2
    fuel += moduleFuel
    fuelForFuel += moduleFuel
    var moduleFuelForFuel = moduleFuel
    while (moduleFuelForFuel > 8)
    {
        moduleFuelForFuel = moduleFuelForFuel/3-2
        fuelForFuel += moduleFuelForFuel
    }
}
println(fuel)
println(fuelForFuel)