package me.cephetir.bladecore.utils.minecraft

import me.cephetir.bladecore.utils.mc
import me.cephetir.bladecore.utils.player
import net.minecraft.entity.Entity
import net.minecraft.potion.Potion
import kotlin.math.hypot

object MovementUtils {

    val isInputting
        get() = player?.movementInput?.let {
            it.moveForward != 0.0f || it.moveStrafe != 0.0f
        } ?: false

    fun getRawDirection(): Float = getRawDirectionRotation(mc.thePlayer.rotationYaw, mc.thePlayer.moveStrafing, mc.thePlayer.moveForward)

    private fun getRawDirectionRotation(yaw: Float, pStrafe: Float, pForward: Float): Float {
        var rotationYaw = yaw
        if (pForward < 0f) rotationYaw += 180f
        var forward = 1f
        if (pForward < 0f) forward = -0.5f else if (pForward > 0f) forward = 0.5f
        if (pStrafe > 0f) rotationYaw -= 90f * forward
        if (pStrafe < 0f) rotationYaw += 90f * forward
        return rotationYaw
    }

    val Entity.isMoving get() = speed > 0.0001
    val Entity.speed get() = hypot(motionX, motionZ)
    val Entity.realSpeed get() = hypot(posX - prevPosX, posZ - prevPosZ)

    fun applySpeedPotionEffects(speed: Double) = player?.getActivePotionEffect(Potion.moveSpeed)?.let { speed * (1.0 + (it.amplifier + 1) * 0.2) } ?: speed
}