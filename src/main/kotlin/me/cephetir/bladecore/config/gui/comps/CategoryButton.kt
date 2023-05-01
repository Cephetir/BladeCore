package me.cephetir.bladecore.config.gui.comps

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.SettingManager
import me.cephetir.bladecore.core.config.BladeConfig
import me.cephetir.bladecore.utils.ColorUtils
import me.cephetir.bladecore.utils.ColorUtils.withAlpha
import me.cephetir.bladecore.utils.minecraft.render.RenderUtils
import me.cephetir.bladecore.utils.minecraft.render.RoundUtils
import me.cephetir.bladecore.utils.minecraft.render.shaders.ShadowUtils

class CategoryButton(private val frame: Frame, val category: SettingManager.Category, private val i: Int) {
    private var hovered = false
    private var current = false
    private var glowStr = 0f
    private var textColor = 0f

    private var scrollProgress = 0f
    private var wait = 0L
    private var lastFrame = -1L
    private var direction = 1L

    fun calcY(scrollPixels: Float): Float = frame.y + 10 + i * (ConfigGui.fontRenderer24.getHeight() + 5) + scrollPixels + ConfigGui.fontRenderer24.getHeight() + 6f

    fun draw(mouseX: Int, mouseY: Int, scrollPixels: Float) {
        val x1 = frame.x + frame.width / 7.5f - frame.width / 10f - 2f
        val y1 = frame.y + 10 + i * (ConfigGui.fontRenderer24.getHeight() + 5) + scrollPixels

        hovered = isHovered(x1 * 2f - 3f, y1 * 2f - 3f, frame.width / 2.5f + 6f, ConfigGui.fontRenderer24.getHeight() * 2f + 6f, mouseX.toFloat(), mouseY.toFloat())
        current = frame.currentCategory == category

        val color1 =
            if (hovered || current) frame.colorBGGlow.withAlpha(255)
            else frame.colorBG.withAlpha(255)

        textColor = RenderUtils.animate(if (hovered || current) 1f else 0f, textColor, 0.2f)
        val color2 = when {
            textColor < 0.1f -> frame.colorSecondary
            textColor > 0.9f -> frame.colorPrimary
            else -> ColorUtils.blendColors(
                floatArrayOf(0f, 1f),
                arrayOf(frame.colorSecondary, frame.colorPrimary),
                textColor
            )
        }

        glowStr = RenderUtils.animate(if (hovered || current) 12f else 0f, glowStr, 0.2f)

        RoundUtils.drawSmoothRoundedRect(
            x1 * 2,
            y1 * 2,
            x1 * 2 + frame.width / 2.5f,
            (y1 + ConfigGui.fontRenderer24.getHeight()) * 2f,
            5f,
            color1.rgb
        )
        RoundUtils.drawRoundedOutline(
            x1 * 2,
            y1 * 2,
            x1 * 2 + frame.width / 2.5f,
            (y1 + ConfigGui.fontRenderer24.getHeight()) * 2f,
            5f,
            2f,
            frame.colorPrimary.rgb
        )
        if (BladeConfig.guiPostProcessing.value && glowStr > 0.5f) ShadowUtils.shadow(
            glowStr,
            {
                RoundUtils.drawRoundedRect(
                    x1 * 2,
                    y1 * 2,
                    x1 * 2 + frame.width / 2.5f,
                    (y1 + ConfigGui.fontRenderer24.getHeight()) * 2f,
                    5f,
                    frame.colorPrimary.rgb
                )
            },
            {
                RoundUtils.drawRoundedRect(
                    x1 * 2,
                    y1 * 2,
                    x1 * 2 + frame.width / 2.5f,
                    (y1 + ConfigGui.fontRenderer24.getHeight()) * 2f,
                    5f
                )
            }
        )

        val width = frame.width / 2.5f
        val scroll = width <= ConfigGui.fontRenderer24.getStringWidth(category.name) + 3
        if (!scroll) ConfigGui.fontRenderer24.drawString(
            category.name,
            (frame.x + frame.width / 7.5f - ConfigGui.fontRenderer24.getStringWidth(category.name) / 2f / 2f - 1.5f) * 2.0,
            (y1 + ConfigGui.fontRenderer24.getHeight() / 2f / 2f - 0.5) * 2.0,
            color2
        )
        else {
            val currTime = System.currentTimeMillis()
            if (hovered) {
                if (currTime - wait > 1000) {
                    if (lastFrame == -1L)
                        wait = currTime
                    else {
                        val delta = currTime - lastFrame
                        scrollProgress += 0.0003f * delta * direction
                        if (scrollProgress <= 0f || width * scrollProgress + width - 4 > ConfigGui.fontRenderer24.getStringWidth(category.name)) {
                            direction *= -1
                            wait = currTime
                        }
                    }
                }
            } else {
                scrollProgress = 0f
                direction = 1
                wait = currTime
            }

            RenderUtils.interceptScissor(
                x1 * 2,
                y1 * 2,
                x1 * 2 + frame.width / 2.5f,
                (y1 + ConfigGui.fontRenderer24.getHeight()) * 2f
            )
            ConfigGui.fontRenderer24.drawString(
                category.name,
                (frame.x + frame.width / 7.5f - ConfigGui.fontRenderer24.getStringWidth(category.name).coerceAtMost(width.toInt() - 2) / 2f / 2f - 1.5f) * 2.0 - width * scrollProgress,
                (y1 + ConfigGui.fontRenderer24.getHeight() / 2f / 2f - 0.5) * 2.0,
                color2
            )
        }

        lastFrame = System.currentTimeMillis()
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (hovered)
            frame.setCurrCategory(category)
    }

    fun isHovered(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float) = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
}