package me.bardy.propagator

data class ChunkPosition(
    val x: Int,
    val z: Int
) : Comparable<ChunkPosition> {

    constructor(encoded: Long) : this(encoded.toInt(), (encoded shr 32).toInt())

    fun toLong() = toLong(x, z)

    override fun compareTo(other: ChunkPosition): Int {
        val xCompared = x.compareTo(other.x)
        if (xCompared != 0) return xCompared
        return z.compareTo(other.z)
    }

    companion object {

        @JvmField val ZERO = ChunkPosition(0, 0)
        @JvmField val INVALID = toLong(1875016, 1875016)

        @JvmStatic
        fun toLong(x: Int, z: Int) = x.toLong() and 4294967295L or (z.toLong() and 4294967295L shl 32)
    }
}
