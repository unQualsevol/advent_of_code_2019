import java.io.File

val wide = 25
val tall = 6
val imageSize = wide*tall
val transparent = '2'

val input =File("input").readText()
val layers = input.chunked(imageSize)

for (i in 0..tall-1) {
    for (j in 0..wide - 1) {
        val index = i * wide + j
        for (layer in layers) {
            if (layer[index] != transparent) {
                print(if(layer[index] == '0') ' ' else '#')
                break
            }
        }
    }
    println()
}
