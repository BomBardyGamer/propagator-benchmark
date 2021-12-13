package me.bardy.ticket

class TicketType<T>(
    val name: String,
    val timeout: Long = 0L,
    val comparator: Comparator<T>
) {

    constructor(name: String, comparator: Comparator<T>) : this(name, 0L, comparator)

    override fun toString() = "TicketType(name=$name)"
}
