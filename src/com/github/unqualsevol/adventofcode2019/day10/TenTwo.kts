import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.atan2

val lines = File("input").readLines()

val asteroids = mutableListOf<Vector>()

lines.forEachIndexed { y: Int, line: String ->
    line.forEachIndexed { x: Int, c: Char ->
        if (c == '#') asteroids.add(Vector(x, y))
    }
}


val baseAsteriod = Vector(17, 22)
val asteroidMap = mutableMapOf<Double, MutableList<Asteroid>>()
val vectorizedAsteroids =
        asteroids.filter { it != baseAsteriod }.map { otherAsteroid ->
            Asteroid(
                    otherAsteroid,
                    baseAsteriod.diff(otherAsteroid).minimize(),
                    baseAsteriod.distanceTo(otherAsteroid))

        }
vectorizedAsteroids.forEach { asteroid ->
    run {
        asteroidMap.getOrPut(asteroid.degree(), { mutableListOf() }).add(asteroid)
    }
}

asteroidMap.forEach { (_, value) -> value.sortBy { it.distance } }

var count = 0
val from90To0 = asteroidMap.filter { it.key >= -Math.PI / 2 && it.key < 0 }.toSortedMap()
val from360To180 = asteroidMap.filter { it.key >= 0 }.toSortedMap()
val from180To90 = asteroidMap.filter { it.key >= -Math.PI && it.key < -Math.PI / 2 }.toSortedMap()

while (count < 200) {
    for (values in from90To0.values) {
        if (values.isNotEmpty()) {
            val current = values.removeAt(0)
            count++
            if (count == 200) println("Asteroid: $current")
        }
    }

    for (values in from360To180.values) {
        if (values.isNotEmpty()) {
            val current = values.removeAt(0)
            count++
            if (count == 200) println("Asteroid: $current")
        }
    }

    for (values in from180To90.values) {
        if (values.isNotEmpty()) {
            val current = values.removeAt(0)
            count++
            if (count == 200) println("Asteroid: $current")
        }
    }
}

data class Asteroid(val coor: Vector, val vector: Vector, val distance: Int) {

    fun degree(): Double {
        var baseValue = atan2(vector.y.toDouble(), vector.x.toDouble())
        return baseValue
    }
}


data class Vector(val x: Int, val y: Int) {


    fun distanceTo(vector: Vector): Int {
        val result = (this.x - vector.x).absoluteValue + (this.y - vector.y).absoluteValue
        return result
    }

    fun diff(vector: Vector): Vector = Vector(vector.x - this.x, vector.y - this.y)

    fun minimize(): Vector {
        val mcd = gcd(x.absoluteValue, y.absoluteValue)
        val minX = x / mcd
        val minY = y / mcd

        return Vector(minX, minY)
    }

    private fun gcd(a: Int, b: Int): Int {
        if (b == 0) return a
        return gcd(b, a % b)
    }
}

