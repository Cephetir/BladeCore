package me.cephetir.bladecore.utils.minecraft

import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.event.events.PacketEvent
import me.cephetir.bladecore.core.event.listener.listener
import me.cephetir.bladecore.utils.CircularArray
import me.cephetir.bladecore.utils.CircularArray.Companion.average
import net.minecraft.network.play.server.S03PacketTimeUpdate
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object TpsCalculator {
    private val tickRates = CircularArray(120, 20.0f)

    private var timeLastTimeUpdate = -1L

    val tickRate: Float
        get() = tickRates.average()

    val adjustTicks: Float
        get() = tickRates.average() - 20.0f

    val multiplier: Float
        get() = 20.0f / tickRate

    init {
        listener<PacketEvent.Receive> {
            if (it.packet !is S03PacketTimeUpdate) return@listener

            if (timeLastTimeUpdate != -1L) {
                val timeElapsed = (System.nanoTime() - timeLastTimeUpdate) / 1E9
                tickRates.add((20.0 / timeElapsed).coerceIn(0.0, 20.0).toFloat())
            }

            timeLastTimeUpdate = System.nanoTime()
        }

        listener<FMLNetworkEvent.ServerConnectionFromClientEvent> { reset() }

        BladeEventBus.subscribe(this)
    }

    private fun reset() {
        tickRates.clear()
        timeLastTimeUpdate = -1L
    }
}