package me.bardy.ticket

import java.util.UUID

object TicketTypes {

    val START = TicketType<Unit>("start") { _, _ -> 0 }
    val PLAYER = TicketType<UUID>("player", UUID::compareTo)
}
