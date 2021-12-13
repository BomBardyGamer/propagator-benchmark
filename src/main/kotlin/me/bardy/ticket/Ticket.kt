package me.bardy.ticket

data class Ticket<T>(
    val type: TicketType<T>,
    val level: Int,
    val key: T
) : Comparable<Ticket<*>> {

    @Suppress("UNCHECKED_CAST")
    override fun compareTo(other: Ticket<*>): Int {
        val i = level.compareTo(other.level)
        if (i != 0) return i
        val j = System.identityHashCode(type).compareTo(System.identityHashCode(other.type))
        return if (j != 0) j else type.comparator.compare(key, other.key as T)
    }
}
