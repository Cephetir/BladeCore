package me.cephetir.bladecore.core.event.events

import me.cephetir.bladecore.core.event.Cancellable
import me.cephetir.bladecore.core.event.Event
import me.cephetir.bladecore.core.event.ICancellable
import net.minecraft.network.Packet

abstract class PacketEvent(val packet: Packet<*>) : Event {
    class Receive(packet: Packet<*>) : PacketEvent(packet), ICancellable by Cancellable()
    class PostReceive(packet: Packet<*>) : PacketEvent(packet)
    class Send(packet: Packet<*>) : PacketEvent(packet), ICancellable by Cancellable()
    class PostSend(packet: Packet<*>) : PacketEvent(packet)
}