package me.cephetir.bladecore.remote.metrics

import com.google.gson.JsonObject
import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.event.listener.listener
import me.cephetir.bladecore.utils.mc
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.nio.charset.StandardCharsets
import java.util.*

internal object Metrics {
    private var client = MetricsClient()

    fun send(message: String) {
        client.send(message)
    }

    fun connect() {
        try {
            val obj = JsonObject()
            obj.addProperty("uuid", mc.session.profile.id.toString())

            client.addHeader("auth", Base64.getEncoder().encodeToString(obj.toString().toByteArray(StandardCharsets.UTF_8)))
            client.connect()
        } catch (ex: Exception) {
            BladeCore.logger.warn("Failed to connect to Metics Web Socket!")
            ex.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            val obj = JsonObject()
            obj.addProperty("uuid", mc.session.profile.id.toString())
            obj.addProperty("reason", "close")

            val encrypted = Base64.getEncoder().encodeToString(obj.toString().toByteArray(StandardCharsets.UTF_8))
            send(encrypted)
        } catch (ex: Exception) {
            BladeCore.logger.warn("Failed to send disconnect message to Web Socket!")
            ex.printStackTrace()
        }

        client.close()
    }

    private var ticks = 0
    init {
        listener<ClientTickEvent> {
            if (it.phase != TickEvent.Phase.START) return@listener

            if (client.closed && client.closeReasonRemote && ++ticks >= 600) {
                ticks = 0
                client.reconnect()
            }
        }

        BladeEventBus.subscribe(this)
    }
}