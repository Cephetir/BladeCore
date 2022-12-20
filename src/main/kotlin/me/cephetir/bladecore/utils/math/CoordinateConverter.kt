package me.cephetir.bladecore.utils.math

import net.minecraft.util.BlockPos

object CoordinateConverter {
    /**
     * More efficient impl of [BlockPos.toString]
     */
    fun BlockPos.asString(): String = "${this.x}, ${this.y}, ${this.z}"

    fun toCurrent(dimension: Int, pos: BlockPos): BlockPos = when (dimension) {
        -1 -> toOverworld(pos) // Nether to overworld
        0 -> toNether(pos) // Overworld to nether
        else -> pos // End or custom dimension by server
    }

    fun bothConverted(dimension: Int, pos: BlockPos): String = when (dimension) {
        -1 -> "${toOverworld(pos).asString()} (${pos.asString()})"
        0 -> "${pos.asString()} (${toNether(pos).asString()})"
        else -> pos.asString()
    }

    private fun toNether(pos: BlockPos): BlockPos = BlockPos(pos.x / 8, pos.y, pos.z / 8)

    private fun toOverworld(pos: BlockPos): BlockPos = BlockPos(pos.x * 8, pos.y, pos.z * 8)
}