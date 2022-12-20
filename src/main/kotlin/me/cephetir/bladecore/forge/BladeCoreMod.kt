package me.cephetir.bladecore.forge

import me.cephetir.bladecore.BladeCore
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
    modid = BladeCore.MODID,
    name = BladeCore.MOD_NAME,
    version = BladeCore.VERSION,
    acceptedMinecraftVersions = "[1.8.9]",
    modLanguageAdapter = "me.cephetir.bladecore.utils.KotlinAdapter",
    clientSideOnly = true
)
class BladeCoreMod {
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        BladeCore.init()
    }
}