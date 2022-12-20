package me.cephetir.bladecore.utils.minecraft

import me.cephetir.bladecore.utils.mc
import net.minecraft.client.settings.GameSettings
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

object KeybindUtils {
    fun KeyBinding.isDown(inGui: Boolean = false) =
        if (this.keyCode == Keyboard.KEY_NONE || (!inGui && mc.currentScreen != null)) false
        else GameSettings.isKeyDown(this)
}