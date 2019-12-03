import java.io.File
import java.lang.IllegalArgumentException
import java.lang.Integer.min
import kotlin.math.absoluteValue

val wirePaths = mutableMapOf<Coordinate, WireSection>()
val startCoordinate = Coordinate(0, 0)
var closestIntersection = Coordinate(Int.MAX_VALUE, 0)
var lowestStepsToIntersection = Int.MAX_VALUE
var wireId = 0
File("input").forEachLine {
    wireId++
    val wireLines = it.split(",")
    var currentCoordinate = startCoordinate
    var stepCount = 0
    wireLines.forEach { wireLine ->
        val direction = wireLine.subSequence(0,1).toString()
        val length = wireLine.subSequence(1, wireLine.length).toString().toInt()
        for (i in 1..length) {
            stepCount++
            currentCoordinate =
                    when(direction) {
                        "R" -> Coordinate(currentCoordinate.x+1, currentCoordinate.y)
                        "L" -> Coordinate(currentCoordinate.x-1, currentCoordinate.y)
                        "U" -> Coordinate(currentCoordinate.x, currentCoordinate.y+1)
                        "D" -> Coordinate(currentCoordinate.x, currentCoordinate.y-1)
                        else -> throw IllegalArgumentException("not a valid operation")
                    }

            if(wirePaths.containsKey(currentCoordinate)) {
                val previousValue = wirePaths.get(currentCoordinate)
                previousValue?.let {
                    if (it.wireId != wireId) {
                        val stepsToIntersection = stepCount + it.step

                        println("x: ${currentCoordinate.x} y: ${currentCoordinate.y} distance: ${startCoordinate.distanceTo(currentCoordinate)}")
                        println("previousValue: $it stepCount: $stepCount stepsToIntersection: $stepsToIntersection")
                        lowestStepsToIntersection = min(lowestStepsToIntersection, stepsToIntersection)

                        if (startCoordinate.distanceTo(currentCoordinate) < startCoordinate.distanceTo(closestIntersection)) {
                            closestIntersection = currentCoordinate
                        }
                    }
                }
            } else {
                wirePaths.put(currentCoordinate, WireSection(wireId, stepCount))
            }
        }
    }
}

println("x: ${closestIntersection.x} y: ${closestIntersection.y} distance: ${startCoordinate.distanceTo(closestIntersection)}")
println("lowestStepsToIntersection: $lowestStepsToIntersection")

data class Coordinate(val x: Int, val y: Int) {

    fun distanceTo(coordinate: Coordinate): Int {
        val result = (this.x - coordinate.x).absoluteValue + (this.y - coordinate.y).absoluteValue
        return result
    }

}


data class WireSection(val wireId: Int, val step: Int)