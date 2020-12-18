val input = "372304-847060"

val start = input.substringBefore("-").toString().toInt()
val end = input.substringAfter("-").toString().toInt()


val regex = """(00|11|22|33|44|55|66|77|88|99)""".toRegex()

var count = 0
for (i in start..end) {
    val currentKey = i.toString()
    val toCharArray = currentKey.toCharArray()
    toCharArray.sort()
    val sortedKey = toCharArray.joinToString("")

    if (currentKey == sortedKey && regex.containsMatchIn(currentKey)) {
        val uniqueMatches = regex.findAll(currentKey).distinctBy { it.value }
        for (mr in uniqueMatches){
            val currentDigit = mr.value.get(0)
            if (currentKey.count { c -> c == currentDigit } == 2) {
                count++
                break
            }
        }
    }
}
println("count of matches: $count")