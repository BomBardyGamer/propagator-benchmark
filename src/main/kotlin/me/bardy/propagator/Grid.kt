package me.bardy.propagator

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import javax.swing.JPanel
import java.awt.Color
import java.awt.Graphics
import java.awt.Point

class Grid<T>(
    private val offsetX: Int,
    private val offsetY: Int,
    private val colorCalculator: (T) -> Color
) : JPanel() {

    private val cells = Long2ObjectOpenHashMap<T>()

    fun add(x: Int, y: Int, value: T) {
        cells[ChunkPosition.toLong(x - offsetX, y - offsetY)] = value
        repaint()
    }

    fun reset() {
        cells.clear()
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        cells.long2ObjectEntrySet().fastForEach {
            val cellX = it.longKey.toInt() * OFFSET
            val cellY = (it.longKey shr 32).toInt() * OFFSET
            g.color = Color.BLACK
            g.drawRect(cellX, cellY, SIZE, SIZE)
            g.color = colorCalculator(it.value)
            g.fillRect(cellX, cellY, SIZE, SIZE)
            g.color = Color.BLACK
            g.drawString(it.value.toString(), cellX + (OFFSET / 10), cellY + (OFFSET / 2))
        }
    }

    companion object {

        private const val OFFSET = 30
        private const val SIZE = 20
    }
}
