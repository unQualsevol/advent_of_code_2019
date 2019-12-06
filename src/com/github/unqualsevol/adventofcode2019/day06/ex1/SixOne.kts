import java.io.File

val orbitalsMap = mutableMapOf<String, MutableList<String>>()
val orbitingObjects = mutableSetOf<String>()
File("input").forEachLine {
    val (planet, satellite) = it.split(")")
    orbitingObjects.add(satellite)
    if(orbitalsMap.containsKey(planet)){
        val orbitingList = orbitalsMap.get(planet)!!
        orbitingList.add(satellite)
        orbitalsMap.put(planet, orbitingList)
    } else {
        orbitalsMap.put(planet, mutableListOf(satellite))
    }
}

val root = "COM"

val result = dfs(root, 0)

println(result)

fun dfs (root: String, level: Int) : Int
{
    val children = orbitalsMap[root]
    var orbitCount = level
    children?.let {
        it.forEach {
            orbitCount += dfs(it, level+1)
        }
    }
    return orbitCount
}