import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.atanh
import kotlin.math.max

val lines = File("input").readLines()

val asteroids = mutableListOf<Vector>()

lines.forEachIndexed {
    y : Int, line: String ->
    line.forEachIndexed {
        x : Int, c: Char -> if (c == '#') asteroids.add(Vector(x, y))
    }
}

var maxVisibleAsteroids = 0
var bestBaseAsteroid = Vector(0,0)
asteroids.forEach { baseAsteriod ->
    run {
        val visibleAsteroids = mutableSetOf<Vector>()
        asteroids.forEach{otherAsteroid ->
            run {
                if(baseAsteriod != otherAsteroid) {
                    visibleAsteroids.add(baseAsteriod.diff(otherAsteroid).minimize())
                }
            }
        }
        if(maxVisibleAsteroids < visibleAsteroids.size) {
            maxVisibleAsteroids = visibleAsteroids.size
            bestBaseAsteroid = baseAsteriod
        }
    }
}

println("maxVisibleAsteroids: $maxVisibleAsteroids bestBaseAsteroid: $bestBaseAsteroid")



data class Vector(val x: Int, val y: Int) {

    fun diff(vector: Vector) : Vector = Vector(vector.x - this.x, vector.y-this.y)

    fun minimize() : Vector {
        val mcd = gcd(x.absoluteValue, y.absoluteValue)
        val minX = x/mcd
        val minY = y/mcd

        return Vector(minX, minY)
    }

    private fun gcd(a: Int, b: Int): Int {
        if (b == 0) return a
        return gcd(b, a % b)
    }
}

