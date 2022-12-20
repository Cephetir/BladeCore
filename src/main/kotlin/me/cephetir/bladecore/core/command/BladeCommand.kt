package me.cephetir.bladecore.core.command

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import kotlinx.coroutines.launch
import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.core.config.BladeConfig
import me.cephetir.bladecore.utils.HttpUtils
import me.cephetir.bladecore.utils.minecraft.ChatUtils
import me.cephetir.bladecore.utils.minecraft.GuiUtils
import me.cephetir.bladecore.utils.threading.BackgroundScope
import me.cephetir.bladecore.utils.threading.onMainThread

object BladeCommand : Command(BladeCore.MODID, true) {
    override val commandAliases = setOf(Alias("bc"), Alias("belgianCutie"))

    @DefaultHandler
    fun handle() {
        GuiUtils.openScreen(BladeConfig.gui() ?: return ChatUtils.msg("§cFailed to open GUI screen!"))
    }

    @SubCommand("config", description = "Opens the config GUI for " + BladeCore.MOD_NAME)
    fun config() {
        GuiUtils.openScreen(BladeConfig.gui() ?: return ChatUtils.msg("§cFailed to open GUI screen!"))
    }

    @SubCommand("changelog", description = "Shows changelog for the last ${BladeCore.MOD_NAME} update")
    fun changelog() {
        BackgroundScope.launch {
            val changelog = HttpUtils.sendGet("https://bladecore.ilarea.ru/changelog", null) ?: return@launch

            onMainThread {
                ChatUtils.msg(
                    "§6Changes in latest release:\n§a${
                        changelog.split("\n").joinToString("\n§a") {
                            val last = it.toCharArray().last()
                            if (!last.isLetterOrDigit()) it.dropLast(1)
                            else it
                        }
                    }"
                )
            }.join()
        }
    }
}