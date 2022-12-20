package me.cephetir.bladecore.utils.minecraft

import me.cephetir.bladecore.utils.mc
import net.minecraft.util.ChatComponentText

internal object ChatUtils {
    fun chat(text: String) = mc.ingameGUI.chatGUI.printChatMessage(ChatComponentText(text))

    fun msg(text: String) = chat("§4[BladeCore] $text")
}