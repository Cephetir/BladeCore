package me.cephetir.bladecore.discord

import com.jagrosh.discordipc.entities.RichPresence

interface IPC {
    fun handleConnect()
    fun handleDisconnect()
    fun updateRPC(): RichPresence
}