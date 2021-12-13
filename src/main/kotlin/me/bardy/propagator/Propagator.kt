package me.bardy.propagator

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import me.bardy.ticket.SortedArraySet
import me.bardy.ticket.Ticket
import me.bardy.ticket.TicketType
import me.bardy.ticket.TicketTypes
import java.util.UUID
import kotlin.math.abs
import kotlin.math.max

class Propagator {

    val tickets: Long2ObjectMap<SortedArraySet<Ticket<*>>> = Long2ObjectMaps.synchronize(Long2ObjectOpenHashMap())

    fun <T> addTicket(x: Int, z: Int, type: TicketType<T>, level: Int, key: T) {
        if (type === TicketTypes.PLAYER) return
        propagate(x, z, Ticket(type, level, key))
    }

    fun <T> removeTicket(x: Int, z: Int, type: TicketType<T>, level: Int, key: T) {
        if (type === TicketTypes.PLAYER) return
        val ticket = Ticket(type, level, key)
        tickets[ChunkPosition.toLong(x, z)]?.remove(ticket)
        val radius = MAXIMUM_TICKET_LEVEL - level + 1
        reset(x, z, type, key, radius)
    }

    fun addPlayer(x: Int, z: Int, oldX: Int, oldZ: Int, uuid: UUID, viewDistance: Int) {
        if (x != oldX || z != oldZ) removePlayer(oldX, oldZ, uuid, viewDistance)
        propagateView(x, z, uuid, viewDistance)
    }

    fun removePlayer(x: Int, z: Int, uuid: UUID, viewDistance: Int) = reset(x, z, TicketTypes.PLAYER, uuid, viewDistance, OFFSET)

    private fun <T> propagate(x: Int, z: Int, ticket: Ticket<T>) {
        var i = 0
        while (true) {
            val pos = chunkInSpiral(i, x, z)
            val xo = pos.toInt()
            val zo = (pos shr 32).toInt()
            val calculatedLevel = calculateLevel(absDelta(xo - x, zo - z), ticket.level)
            if (calculatedLevel > MAXIMUM_TICKET_LEVEL) break
            tickets.getOrPut(pos) { SortedArraySet.create(4) }.add(Ticket(ticket.type, calculatedLevel, ticket.key))
            i++
        }
    }

    private fun propagateView(x: Int, z: Int, uuid: UUID, viewDistance: Int) {
        var i = 0
        while (true) {
            val pos = chunkInSpiral(i, x, z)
            val xo = pos.toInt()
            val zo = (pos shr 32).toInt()
            val absDelta = absDelta(xo - x, zo - z)
            val calculatedLevel = if (absDelta <= viewDistance) PLAYER_TICKET_LEVEL else calculateLevel(absDelta - viewDistance, PLAYER_TICKET_LEVEL)
            if (calculatedLevel > MAXIMUM_TICKET_LEVEL) break
            tickets.getOrPut(pos) { SortedArraySet.create(4) }.add(Ticket(TicketTypes.PLAYER, calculatedLevel, uuid))
            i++
        }
    }

    private fun <T, K> reset(x: Int, z: Int, type: TicketType<T>, key: K, radius: Int, offset: Int = 0) {
        for (i in 0 until (radius * 2 + offset) * (radius * 2 + offset)) {
            val pos = chunkInSpiral(i, x, z)
            val list = tickets[pos] ?: continue
            list.removeIf { it.type === type && it.key === key }
            if (list.isEmpty()) tickets.remove(pos)
        }
    }

    companion object {

        private const val PLAYER_TICKET_LEVEL = 31
        private const val MAXIMUM_TICKET_LEVEL = 44
        private const val OFFSET = (MAXIMUM_TICKET_LEVEL - PLAYER_TICKET_LEVEL) * 2 + 1
    }
}

private fun absDelta(deltaX: Int, deltaZ: Int): Int {
    if (deltaX == 0 && deltaZ == 0) return 0
    return max(abs(deltaX), abs(deltaZ))
}

private fun calculateLevel(absDelta: Int, center: Int): Int = if (absDelta >= 0) center + absDelta else center - absDelta
