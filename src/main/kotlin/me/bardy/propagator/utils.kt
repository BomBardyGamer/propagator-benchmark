package me.bardy.propagator

import kotlin.math.sqrt

fun chunkInSpiral(id: Int, xOffset: Int = 0, zOffset: Int = 0): Long {
    if (id == 0) return ChunkPosition.toLong(0 + xOffset, 0 + zOffset)
    val index = id - 1
    val radius = ((sqrt(index + 1.0) - 1) / 2).floor() + 1
    val p = 8 * radius * (radius - 1) / 2
    val en = radius * 2
    val a = (1 + index - p) % (radius * 8)
    return when (a / (radius * 2)) {
        0 -> ChunkPosition.toLong(a - radius + xOffset, -radius + zOffset)
        1 -> ChunkPosition.toLong(radius + xOffset, a % en - radius + zOffset)
        2 -> ChunkPosition.toLong(radius - a % en + xOffset, radius + zOffset)
        3 -> ChunkPosition.toLong(-radius + xOffset, radius - a % en + zOffset)
        else -> ChunkPosition.ZERO.toLong()
    }
}

fun Double.floor(): Int {
    val result = toInt()
    return if (this < result) result - 1 else result
}
