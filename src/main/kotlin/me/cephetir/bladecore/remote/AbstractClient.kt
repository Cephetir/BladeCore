package me.cephetir.bladecore.remote

import me.cephetir.bladecore.BladeCore
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

abstract class AbstractClient(private val name: String, url: URI) : WebSocketClient(url) {
    var closed = false
    var closeReasonRemote = false

    override fun onOpen(handshakedata: ServerHandshake) {
        BladeCore.logger.info("$name Web Socket was successfully connected!")
        closed = false
        closeReasonRemote = false
    }

    override fun onMessage(message: String) {
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        BladeCore.logger.info("$name Web Socket connection closed!")
        closeReasonRemote = remote
        closed = true
    }

    override fun onError(ex: Exception) {
        BladeCore.logger.error("$name Web Socket received error!", ex)
    }
}