package me.bardy.ticket

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap

class Propagator {

    val levelMap = Long2IntOpenHashMap().apply { defaultReturnValue(-1) }

    fun checkEdge(x: Int, z: Int, level: Int, callback: (Int, Int, Int) -> Unit) {
        val source = ChunkPosition.toLong(x, z)
        val existing = levelMap[source]
        if (existing == -1 || level < existing) {
            levelMap[source] = level
            callback(x, z, level)
        }
        Direction2D.values().forEach { checkNeighbour(x + it.offsetX, z + it.offsetZ, it.offsetX, it.offsetZ, level + 1, callback) }
    }

    private fun checkNeighbour(x: Int, z: Int, offsetX: Int, offsetZ: Int, level: Int, callback: (Int, Int, Int) -> Unit) {
        if (level > 44) return
        val pos = ChunkPosition.toLong(x, z)
        val existing = levelMap[pos]
        if (existing == -1 || level < existing) {
            levelMap[pos] = level
            callback(x, z, level)
        }
        if (offsetX != 0 && offsetZ != 0) {
            checkNeighbour(if (offsetX > 0) x + 1 else x - 1, z, offsetX, 0, level + 1, callback)
            checkNeighbour(x, if (offsetZ > 0) z + 1 else z - 1, 0, offsetZ, level + 1, callback)
            checkNeighbour(if (offsetX > 0) x + 1 else x - 1, if (offsetZ > 0) z + 1 else z - 1, offsetX, offsetZ, level + 1, callback)
        } else if (offsetX != 0) {
            checkNeighbour(if (offsetX > 0) x + 1 else x - 1, z, offsetX, 0, level + 1, callback)
        } else if (offsetZ != 0) {
            checkNeighbour(x, if (offsetZ > 0) z + 1 else z - 1, 0, offsetZ, level + 1, callback)
        } else {
            return // No neighbours to check
        }
    }
}
