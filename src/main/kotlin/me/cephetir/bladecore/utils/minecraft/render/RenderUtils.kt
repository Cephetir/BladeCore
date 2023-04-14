package me.cephetir.bladecore.utils.minecraft.render

import me.cephetir.bladecore.utils.mc
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object RenderUtils {
    fun setColor(color: Color, alpha: Float) {
        val (red, green, blue) = color.getColorComponents(null)
        GlStateManager.color(red, green, blue, alpha)
    }

    @JvmStatic
    fun drawRect(left: Float, top: Float, right: Float, bottom: Float, color: Color) {
        var left = left
        var top = top
        var right = right
        var bottom = bottom
        if (left < right) {
            val i = left
            left = right
            right = i
        }
        if (top < bottom) {
            val j = top
            top = bottom
            bottom = j
        }
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        setColor(color, color.alpha / 255f)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(left.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldrenderer.pos(right.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldrenderer.pos(right.toDouble(), top.toDouble(), 0.0).endVertex()
        worldrenderer.pos(left.toDouble(), top.toDouble(), 0.0).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    @JvmStatic
    fun outline(x1: Float, y1: Float, x2: Float, y2: Float, width: Float, color: Color) {
        drawRect(x1, y1, x2 - width, y1 + width, color)
        drawRect(x2 - width, y1, x2, y2, color)
        drawRect(x1, y2 - width, x2 - width, y2, color)
        drawRect(x1, y1 + width, x1 + width, y2 - width, color)
    }

    @JvmStatic
    fun quickDrawRect(x: Float, y: Float, x2: Float, y2: Float) {
        GL11.glBegin(GL11.GL_QUADS)
        GL11.glVertex2d(x2.toDouble(), y.toDouble())
        GL11.glVertex2d(x.toDouble(), y.toDouble())
        GL11.glVertex2d(x.toDouble(), y2.toDouble())
        GL11.glVertex2d(x2.toDouble(), y2.toDouble())
        GL11.glEnd()
    }

    @JvmStatic
    fun drawImage(image: ResourceLocation, x: Float, y: Float, width: Int, height: Int) {
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDepthMask(false)
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glTranslatef(x, y, x)
        mc.textureManager.bindTexture(image)
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, width, height, width.toFloat(), height.toFloat())
        GL11.glTranslatef(-x, -y, -x)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
    }

    @JvmStatic
    fun drawCircle(x: Float, y: Float, radius: Float, lineWidth: Float, start: Int, end: Int, color: Color) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        setColor(color, color.alpha.toFloat())
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(lineWidth)
        GL11.glBegin(GL11.GL_LINE_STRIP)
        var i = end.toFloat()
        while (i >= start) {
            GL11.glVertex2f((x + cos(i * PI / 180) * (radius * 1.001f)).toFloat(), (y + sin(i * PI / 180) * (radius * 1.001f)).toFloat())
            i -= 360f / 90f
        }
        GL11.glEnd()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun drawFilledCircle(xx: Float, yy: Float, radius: Float, color: Color) {
        val sections = 50
        val dAngle = 2 * PI / sections
        var x: Float
        var y: Float
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glBegin(GL11.GL_TRIANGLE_FAN)
        for (i in 0 until sections) {
            x = (radius * sin(i * dAngle)).toFloat()
            y = (radius * cos(i * dAngle)).toFloat()
            GL11.glColor4f(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
            GL11.glVertex2f(xx + x, yy + y)
        }
        GlStateManager.color(0f, 0f, 0f)
        GL11.glEnd()
        GL11.glPopAttrib()
    }

    fun scissor(x: Float, y: Float, x1: Float, y1: Float) {
        val scaleFactor = getScaleFactor()
        GL11.glScissor((x * scaleFactor).toInt(), (mc.displayHeight - y1 * scaleFactor).toInt(), ((x1 - x) * scaleFactor).toInt(), ((y1 - y) * scaleFactor).toInt())
    }

    fun getScaleFactor(): Int {
        var scaleFactor = 1
        val isUnicode = mc.isUnicode
        var guiScale = mc.gameSettings.guiScale
        if (guiScale == 0) guiScale = 1000
        while (scaleFactor < guiScale && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240)
            ++scaleFactor
        if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1)
            --scaleFactor
        return scaleFactor
    }

    fun animate(endPoint: Float, current: Float, speed: Float): Float {
        val sped = speed.coerceIn(0f, 1f)
        val shouldContinueAnimation = endPoint > current
        val dif = endPoint.coerceAtLeast(current) - endPoint.coerceAtMost(current)
        val factor = dif * sped
        return current + if (shouldContinueAnimation) factor else -factor
    }

    fun drawVerticalGradientRect(left: Float, top: Float, right: Float, bottom: Float, startColor: Int, endColor: Int) {
        val startAlpha = (startColor shr 24 and 0xFF).toFloat() / 255.0f
        val startRed = (startColor shr 16 and 0xFF).toFloat() / 255.0f
        val startGreen = (startColor shr 8 and 0xFF).toFloat() / 255.0f
        val startBlue = (startColor and 0xFF).toFloat() / 255.0f
        val endAlpha = (endColor shr 24 and 0xFF).toFloat() / 255.0f
        val endRed = (endColor shr 16 and 0xFF).toFloat() / 255.0f
        val endGreen = (endColor shr 8 and 0xFF).toFloat() / 255.0f
        val endBlue = (endColor and 0xFF).toFloat() / 255.0f
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(right.toDouble(), top.toDouble(), 0.0).color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(left.toDouble(), top.toDouble(), 0.0).color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(left.toDouble(), bottom.toDouble(), 0.0).color(endRed, endGreen, endBlue, endAlpha).endVertex()
        worldrenderer.pos(right.toDouble(), bottom.toDouble(), 0.0).color(endRed, endGreen, endBlue, endAlpha).endVertex()
        tessellator.draw()
        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableTexture2D()
    }

    fun drawHorizontalGradientRect(left: Float, top: Float, right: Float, bottom: Float, startColor: Int, endColor: Int) {
        val startAlpha = (startColor shr 24 and 0xFF).toFloat() / 255.0f
        val startRed = (startColor shr 16 and 0xFF).toFloat() / 255.0f
        val startGreen = (startColor shr 8 and 0xFF).toFloat() / 255.0f
        val startBlue = (startColor and 0xFF).toFloat() / 255.0f
        val endAlpha = (endColor shr 24 and 0xFF).toFloat() / 255.0f
        val endRed = (endColor shr 16 and 0xFF).toFloat() / 255.0f
        val endGreen = (endColor shr 8 and 0xFF).toFloat() / 255.0f
        val endBlue = (endColor and 0xFF).toFloat() / 255.0f
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(right.toDouble(), top.toDouble(), 0.0).color(endRed, endGreen, endBlue, endAlpha).endVertex()
        worldrenderer.pos(left.toDouble(), top.toDouble(), 0.0).color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(left.toDouble(), bottom.toDouble(), 0.0).color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(right.toDouble(), bottom.toDouble(), 0.0).color(endRed, endGreen, endBlue, endAlpha).endVertex()
        tessellator.draw()
        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableTexture2D()
    }
}