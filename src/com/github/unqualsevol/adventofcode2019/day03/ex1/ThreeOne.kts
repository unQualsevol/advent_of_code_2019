import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

val wirePaths = mutableSetOf<Coordinate>()
val startCoordinate = Coordinate(0, 0)
var closestIntersection = Coordinate(Int.MAX_VALUE, 0)
wirePaths.add(startCoordinate)
File("input").forEachLine {
    val wireLines = it.split(",")
    var currentCoordinate = startCoordinate

    wireLines.forEach { wireLine ->
        val direction = wireLine.subSequence(0,1).toString()
        val length = wireLine.subSequence(1, wireLine.length).toString().toInt()
        for (i in 1..length) {
            currentCoordinate =
                    when(direction) {
                        "R" -> Coordinate(currentCoordinate.x+1, currentCoordinate.y)
                        "L" -> Coordinate(currentCoordinate.x-1, currentCoordinate.y)
                        "U" -> Coordinate(currentCoordinate.x, currentCoordinate.y+1)
                        "D" -> Coordinate(currentCoordinate.x, currentCoordinate.y-1)
                        else -> throw IllegalArgumentException("not a valid operation")
                    }
            if(!wirePaths.add(currentCoordinate)
                    && startCoordinate.distanceTo(currentCoordinate) < startCoordinate.distanceTo(closestIntersection)){
                closestIntersection = currentCoordinate
            }
        }
    }
}

println("x: ${closestIntersection.x} y: ${closestIntersection.y} distance: ${startCoordinate.distanceTo(closestIntersection)}")

data class Coordinate(val x: Int, val y: Int) {

    fun distanceTo(coordinate: Coordinate): Int {
        val result = (this.x - coordinate.x).absoluteValue + (this.y - coordinate.y).absoluteValue
        return result
    }
}