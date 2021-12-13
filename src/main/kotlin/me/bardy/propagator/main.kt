package me.bardy.propagator

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import javax.swing.JFrame
import me.bardy.ticket.SortedArraySet
import me.bardy.ticket.Ticket
import me.bardy.ticket.TicketTypes
import java.awt.Color
import java.util.UUID

val GREEN = Color(Color.GREEN.red, Color.GREEN.green, Color.GREEN.blue)
val YELLOW = Color(Color.YELLOW.red, Color.YELLOW.green, Color.YELLOW.blue)
val ORANGE = Color(255, 102, 0)
val RED = Color(Color.RED.red, Color.RED.green, Color.RED.blue)

fun main() {
    val propagator = Propagator()
    propagator.addTicket(10, 10, TicketTypes.START, 22, Unit)
    val grid = Grid<Int>(10, 10) {
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
    val uuid = UUID.randomUUID()
    var lastCentralX = 10
    repeat(20) {
        grid.reset()
        propagator.addPlayer(lastCentralX - 1, 10, lastCentralX, 10, uuid, 10)
        lastCentralX--
        grid.addAll(propagator.tickets)
        Thread.sleep(200)
    }
    Thread.sleep(1500)
    repeat(20) {
        grid.reset()
        propagator.addPlayer(lastCentralX + 1, 10, lastCentralX, 10, uuid, 10)
        lastCentralX++
        grid.addAll(propagator.tickets)
        Thread.sleep(200)
    }
}

private fun Grid<Int>.addAll(ticketMap: Long2ObjectMap<SortedArraySet<Ticket<*>>>) = ticketMap.long2ObjectEntrySet().forEach {
    val tickets = it.value
    if (tickets.isEmpty()) return@forEach
    val x = it.longKey.toInt() + 30
    val z = (it.longKey shr 32).toInt() + 15
    add(x, z, tickets.first!!.level)
}
