import java.io.File

val toParent = mutableMapOf<String, String>()
val toChildren = mutableMapOf<String, MutableList<String>>()
File("input").forEachLine {
    val (planet, satellite) = it.split(")")
    toParent.put(satellite, planet)

    if(toChildren.containsKey(planet)){
        val orbitingList = toChildren.get(planet)!!
        orbitingList.add(satellite)
        toChildren.put(planet, orbitingList)
    } else {
        toChildren.put(planet, mutableListOf(satellite))
    }
}

val origin = "YOU"
val target = "SAN"

val visited = mutableSetOf<String>()

val result = dfs(origin, target, visited, -1)

println(result)

fun dfs(node: String, target: String, visited: MutableSet<String>, distance: Int): Int {

    visited.add(node)
    val children = toChildren[node]
    children?.let {
        it.forEach {
            if(it == target) {
                return distance
            }
            if(!visited.contains(it)){
                val d = dfs(it, target, visited, distance+1)
                if(d > 0) return d
            }
        }
    }

    val parent = toParent[node]
    parent?.let {
        if(it == target)
            return distance
        if(!visited.contains(it))
        {
            val d = dfs(it, target, visited, distance+1)
            if(d > 0) return d
        }
    }
    return 0
}
