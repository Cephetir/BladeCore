package me.cephetir.bladecore

import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins

object BladeCoreInit {
    @JvmStatic
    fun initialize() {
        MixinBootstrap.init()
        Mixins.addConfigurations("mixins.bladecore.json")
        MixinEnvironment.getDefaultEnvironment().obfuscationContext = "searge"

        BladeCore.preInit()
        System.setProperty("bladecore.init", "true")
    }
}