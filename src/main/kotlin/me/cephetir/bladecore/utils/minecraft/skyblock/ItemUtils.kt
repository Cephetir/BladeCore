package me.cephetir.bladecore.utils.minecraft.skyblock

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound


object ItemUtils {
    fun ItemStack.getExtraAttributes(): NBTTagCompound? = this.getSubCompound("ExtraAttributes", false)

    fun ItemStack.getSkyBlockID(): String {
        val extraAttributes = this.getExtraAttributes() ?: return ""
        return extraAttributes.getString("id")
    }
}