package me.cephetir.bladecore.utils

import me.cephetir.bladecore.mixins.accessors.MinecraftAccessor
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.WorldClient

val mc: Minecraft get() = Minecraft.getMinecraft()
val mcAccessor: MinecraftAccessor get() = mc as MinecraftAccessor

val player: EntityPlayerSP? get() = mc.thePlayer

val world: WorldClient? get() = mc.theWorld

