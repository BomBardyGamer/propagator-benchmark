package me.bardy.ticket

import javax.swing.JFrame
import java.awt.Color

val GREEN = Color(Color.GREEN.red, Color.GREEN.green, Color.GREEN.blue)
val YELLOW = Color(Color.YELLOW.red, Color.YELLOW.green, Color.YELLOW.blue)
val ORANGE = Color(255, 102, 0)
val RED = Color(Color.RED.red, Color.RED.green, Color.RED.blue)

fun main() {
    val propagator = Propagator()
    val grid = Grid<Int>(-15, -5) {
        when {
            it <= 31 -> GREEN
            it == 32 -> YELLOW
            it == 33 -> ORANGE
            else -> RED
        }
    }
    JFrame("Ticket propagation view").apply {
        setSize(840, 560)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        add(grid)
        isVisible = true
    }
    propagator.checkEdge(10, 10, 22) { x, z, level -> grid.add(x, z, level) }
    Thread.sleep(2000)
    propagator.checkEdge(15, 15, 24) { x, z, level -> grid.add(x, z, level) }
    Thread.sleep(500)
    propagator.checkEdge(14, 15, 24) { x, z, level -> grid.add(x, z, level) }
    Thread.sleep(500)
    propagator.checkEdge(13, 15, 24) { x, z, level -> grid.add(x, z, level) }
    Thread.sleep(500)
    propagator.checkEdge(12, 15, 24) { x, z, level -> grid.add(x, z, level) }
    Thread.sleep(500)
    propagator.checkEdge(11, 15, 24) { x, z, level -> grid.add(x, z, level) }
}
