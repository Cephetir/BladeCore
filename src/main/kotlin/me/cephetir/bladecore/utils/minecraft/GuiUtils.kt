package me.cephetir.bladecore.utils.minecraft

import kotlinx.coroutines.runBlocking
import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.event.listener.listener
import me.cephetir.bladecore.utils.mc
import me.cephetir.bladecore.utils.threading.onMainThread
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.gameevent.TickEvent

object GuiUtils {
    private class OpenGui(val gui: GuiScreen) {
        var opened = false

        init {
            listener<TickEvent.ClientTickEvent> {
                mc.displayGuiScreen(gui)
                opened = true
                BladeEventBus.unsubscribe(this)
            }

            BladeEventBus.subscribe(this)
        }
    }

    fun openScreen(gui: GuiScreen) = openScreenAsync(gui)

    fun openScreenAsync(gui: GuiScreen) {
        OpenGui(gui)
    }

    fun openScreenSync(gui: GuiScreen) {
        runBlocking {
            onMainThread { mc.displayGuiScreen(gui) }
        }
    }
}
