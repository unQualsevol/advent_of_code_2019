import java.io.File

val wide = 25
val tall = 6

val input =File("input").readText()
val minZeroLayer = input.chunked(25 * 6).minBy { layer -> layer.count { c -> c == '0' } }

val ones = minZeroLayer?.count { c -> c == '1' }
val twos = minZeroLayer?.count { c -> c == '2' }

println("${ones!!*twos!!}")