package me.cephetir.bladecore.utils.minecraft.render

import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object RoundUtils {

    fun enableGL2D() {
        GL11.glDisable(2929)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GL11.glBlendFunc(770, 771)
        GL11.glDepthMask(true)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        GL11.glHint(3155, 4354)
    }

    fun disableGL2D() {
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GL11.glEnable(2929)
        GL11.glDisable(2848)
        GL11.glHint(3154, 4352)
        GL11.glHint(3155, 4352)
    }
    /*
     *
     * NORMAL
     *
     */
    /**
     * @param x      : X pos
     * @param y      : Y pos
     * @param x1     : X2 pos
     * @param y1     : Y2 pos
     * @param radius : round of edges;
     * @param color  : color;
     */
    @JvmStatic
    fun drawSmoothRoundedRect(x: Float, y: Float, x1: Float, y1: Float, radius: Float, color: Int) {
        var x = x
        var y = y
        var x1 = x1
        var y1 = y1
        GL11.glPushAttrib(0)
        GlStateManager.scale(0.5, 0.5, 0.5)
        x *= 2.0.toFloat()
        y *= 2.0.toFloat()
        x1 *= 2.0.toFloat()
        y1 *= 2.0.toFloat()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        setColor(color)
        GL11.glEnable(2848)
        GL11.glBegin(GL11.GL_POLYGON)
        var i: Int = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y + radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y1 - radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y1 - radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y + radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        GL11.glEnd()
        GL11.glBegin(GL11.GL_LINE_LOOP)
        i = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y + radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y1 - radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y1 - radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y + radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        GL11.glEnd()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GL11.glDisable(2848)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
        GlStateManager.scale(2.0, 2.0, 2.0)
        GL11.glPopAttrib()
    }

    @JvmStatic
    fun drawRoundedRect(x: Float, y: Float, x1: Float, y1: Float, radius: Float, color: Int = -1) {
        var x = x
        var y = y
        var x1 = x1
        var y1 = y1
        GlStateManager.pushMatrix()
        GlStateManager.scale(0.5, 0.5, 0.5)
        x *= 2.0.toFloat()
        y *= 2.0.toFloat()
        x1 *= 2.0.toFloat()
        y1 *= 2.0.toFloat()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        if (color != -1) setColor(color)
        GL11.glEnable(2848)
        GL11.glBegin(GL11.GL_POLYGON)
        var i: Int = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y + radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y1 - radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y1 - radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y + radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        GL11.glEnd()
        GlStateManager.disableBlend()
        GL11.glDisable(2848)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
        GlStateManager.scale(2f, 2f, 2f)
        GlStateManager.resetColor()
        GlStateManager.popMatrix()
    }

    /**
     * @param x         : X pos
     * @param y         : Y pos
     * @param x1        : X2 pos
     * @param y1        : Y2 pos
     * @param radius    : round of edges;
     * @param lineWidth : width of outline line;
     * @param color     : color;
     */
    @JvmStatic
    fun drawRoundedOutline(x: Float, y: Float, x1: Float, y1: Float, radius: Float, lineWidth: Float, color: Int) {
        var x = x
        var y = y
        var x1 = x1
        var y1 = y1
        GlStateManager.pushMatrix()
        GlStateManager.scale(0.5, 0.5, 0.5)
        x *= 2.0.toFloat()
        y *= 2.0.toFloat()
        x1 *= 2.0.toFloat()
        y1 *= 2.0.toFloat()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        setColor(color)
        GL11.glEnable(2848)
        GL11.glLineWidth(lineWidth)
        GL11.glBegin(GL11.GL_LINE_LOOP)
        var i: Int = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y + radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                y1 - radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
            i += 3
        }
        i = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y1 - radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x1 - radius + sin(i * Math.PI / 180.0) * radius,
                y + radius + cos(i * Math.PI / 180.0) * radius
            )
            i += 3
        }
        GL11.glEnd()
        GlStateManager.disableBlend()
        GL11.glDisable(2848)
        GlStateManager.enableTexture2D()
        GlStateManager.scale(2f, 2f, 2f)
        GlStateManager.resetColor()
        GlStateManager.popMatrix()
    }
    /*
     *
     * SELECTED EDGES
     *
     */
    /**
     * @param x       : X pos
     * @param y       : Y pos
     * @param x1      : X2 pos
     * @param y1      : Y2 pos
     * @param radius1 : round of left top edges;
     * @param radius2 : round of right top edges;
     * @param radius3 : round of left bottom edges;
     * @param radius4 : round of right bottom edges;
     * @param color   : color;
     */
    @JvmStatic
    fun drawSelectRoundedRect(
        x: Float,
        y: Float,
        x1: Float,
        y1: Float,
        radius1: Float,
        radius2: Float,
        radius3: Float,
        radius4: Float,
        color: Int
    ) {
        var x = x
        var y = y
        var x1 = x1
        var y1 = y1
        GL11.glPushAttrib(0)
        GlStateManager.scale(0.5, 0.5, 0.5)
        x *= 2.0.toFloat()
        y *= 2.0.toFloat()
        x1 *= 2.0.toFloat()
        y1 *= 2.0.toFloat()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        setColor(color)
        GL11.glEnable(2848)
        GL11.glBegin(9)
        var i: Int = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x + radius1 + sin(i * Math.PI / 180.0) * radius1 * -1.0,
                y + radius1 + cos(i * Math.PI / 180.0) * radius1 * -1.0
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x + radius2 + sin(i * Math.PI / 180.0) * radius2 * -1.0,
                y1 - radius2 + cos(i * Math.PI / 180.0) * radius2 * -1.0
            )
            i += 3
        }
        i = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x1 - radius3 + sin(i * Math.PI / 180.0) * radius3,
                y1 - radius3 + cos(i * Math.PI / 180.0) * radius3
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x1 - radius4 + sin(i * Math.PI / 180.0) * radius4,
                y + radius4 + cos(i * Math.PI / 180.0) * radius4
            )
            i += 3
        }
        GL11.glEnd()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GL11.glDisable(2848)
        GlStateManager.disableBlend()
        GlStateManager.enableTexture2D()
        GlStateManager.scale(2.0, 2.0, 2.0)
        GL11.glPopAttrib()
    }

    /**
     * @param x         : X pos
     * @param y         : Y pos
     * @param x1        : X2 pos
     * @param y1        : Y2 pos
     * @param radius1   : round of left top edges;
     * @param radius2   : round of right top edges;
     * @param radius3   : round of left bottom edges;
     * @param radius4   : round of right bottom edges;
     * @param lineWidth : width of outline line;
     * @param color     : color;
     */
    @JvmStatic
    fun drawSelectRoundedOutline(
        x: Float,
        y: Float,
        x1: Float,
        y1: Float,
        radius1: Float,
        radius2: Float,
        radius3: Float,
        radius4: Float,
        lineWidth: Float,
        color: Int
    ) {
        var x = x
        var y = y
        var x1 = x1
        var y1 = y1
        GL11.glPushAttrib(0)
        GlStateManager.scale(0.5, 0.5, 0.5)
        x *= 2.0.toFloat()
        y *= 2.0.toFloat()
        x1 *= 2.0.toFloat()
        y1 *= 2.0.toFloat()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        setColor(color)
        GL11.glEnable(2848)
        GL11.glLineWidth(lineWidth)
        GL11.glBegin(GL11.GL_LINE_LOOP)
        var i: Int = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x + radius1 + sin(i * Math.PI / 180.0) * radius1 * -1.0,
                y + radius1 + cos(i * Math.PI / 180.0) * radius1 * -1.0
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x + radius2 + sin(i * Math.PI / 180.0) * radius2 * -1.0,
                y1 - radius2 + cos(i * Math.PI / 180.0) * radius2 * -1.0
            )
            i += 3
        }
        i = 0
        while (i <= 90) {
            GL11.glVertex2d(
                x1 - radius3 + sin(i * Math.PI / 180.0) * radius3,
                y1 - radius3 + cos(i * Math.PI / 180.0) * radius3
            )
            i += 3
        }
        i = 90
        while (i <= 180) {
            GL11.glVertex2d(
                x1 - radius4 + sin(i * Math.PI / 180.0) * radius4,
                y + radius4 + cos(i * Math.PI / 180.0) * radius4
            )
            i += 3
        }
        GL11.glEnd()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GL11.glDisable(2848)
        GlStateManager.disableBlend()
        GlStateManager.enableTexture2D()
        GlStateManager.scale(2.0, 2.0, 2.0)
        GL11.glPopAttrib()
    }

    fun setColor(color: Int) {
        val a = (color shr 24 and 0xFF) / 255.0f
        val r = (color shr 16 and 0xFF) / 255.0f
        val g = (color shr 8 and 0xFF) / 255.0f
        val b = (color and 0xFF) / 255.0f
        GlStateManager.color(r, g, b, a)
    }
    /*
     *
     * GRADIENT
     *
     */
    /**
     * @param x      : X pos
     * @param y      : Y pos
     * @param x1     : X2 pos
     * @param y1     : Y2 pos
     * @param radius : round of edges;
     * @param color  : color;
     * @param color2 : color2;
     * @param color3 : color3;
     * @param color4 : color4;
     */
    @JvmStatic
    fun drawRoundedGradientRectCorner(
        x: Float,
        y: Float,
        x1: Float,
        y1: Float,
        radius: Float,
        color: Int,
        color2: Int,
        color3: Int,
        color4: Int
    ) {
        var x = x
        var y = y
        var x1 = x1
        var y1 = y1
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)
        GL11.glPushAttrib(0)
        GlStateManager.scale(0.5, 0.5, 0.5)
        x *= 2.0.toFloat()
        y *= 2.0.toFloat()
        x1 *= 2.0.toFloat()
        y1 *= 2.0.toFloat()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        setColor(color)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)
        GL11.glBegin(9)
        var i: Int = 0
        while (i <= 90) {
            setColor(color)
            i += 3
        }
        GL11.glVertex2d(
            x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
            y + radius + cos(i * Math.PI / 180.0) * radius * -1.0
        )
        i = 90
        while (i <= 180) {
            setColor(color2)
            i += 3
        }
        GL11.glVertex2d(
            x + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
            y1 - radius + cos(i * Math.PI / 180.0) * radius * -1.0
        )
        i = 0
        while (i <= 90) {
            setColor(color3)
            i += 3
        }
        GL11.glVertex2d(
            x1 - radius + sin(i * Math.PI / 180.0) * radius,
            y1 - radius + cos(i * Math.PI / 180.0) * radius
        )
        i = 90
        while (i <= 180) {
            setColor(color4)
            i += 3
        }
        GL11.glVertex2d(
            x1 - radius + sin(i * Math.PI / 180.0) * radius,
            y + radius + cos(i * Math.PI / 180.0) * radius
        )
        GL11.glEnd()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GL11.glDisable(2848)
        GlStateManager.disableBlend()
        GlStateManager.enableTexture2D()
        GlStateManager.scale(2.0, 2.0, 2.0)
        GL11.glPopAttrib()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GL11.glDisable(2848)
        GL11.glShadeModel(7424)
    }

    // rTL = radius top left, rTR = radius top right, rBR = radius bottom right, rBL = radius bottom left
    fun customRounded(paramXStart: Float, paramYStart: Float, paramXEnd: Float, paramYEnd: Float, rTL: Float, rTR: Float, rBR: Float, rBL: Float, color: Int) {
        var paramXStart = paramXStart
        var paramYStart = paramYStart
        var paramXEnd = paramXEnd
        var paramYEnd = paramYEnd
        val alpha = (color shr 24 and 0xFF) / 255.0f
        val red = (color shr 16 and 0xFF) / 255.0f
        val green = (color shr 8 and 0xFF) / 255.0f
        val blue = (color and 0xFF) / 255.0f
        var z: Float
        if (paramXStart > paramXEnd) {
            z = paramXStart
            paramXStart = paramXEnd
            paramXEnd = z
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart
            paramYStart = paramYEnd
            paramYEnd = z
        }
        val xTL = (paramXStart + rTL).toDouble()
        val yTL = (paramYStart + rTL).toDouble()
        val xTR = (paramXEnd - rTR).toDouble()
        val yTR = (paramYStart + rTR).toDouble()
        val xBR = (paramXEnd - rBR).toDouble()
        val yBR = (paramYEnd - rBR).toDouble()
        val xBL = (paramXStart + rBL).toDouble()
        val yBL = (paramYEnd - rBL).toDouble()
        GL11.glPushMatrix()
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(1f)
        GL11.glColor4f(red, green, blue, alpha)
        GL11.glBegin(GL11.GL_POLYGON)
        val degree = PI / 180
        run {
            var i = 0.0
            while (i <= 90) {
                GL11.glVertex2d(xBR + sin(i * degree) * rBR, yBR + cos(i * degree) * rBR)
                i += 1.0
            }
        }
        run {
            var i = 90.0
            while (i <= 180) {
                GL11.glVertex2d(xTR + sin(i * degree) * rTR, yTR + cos(i * degree) * rTR)
                i += 1.0
            }
        }
        run {
            var i = 180.0
            while (i <= 270) {
                GL11.glVertex2d(xTL + sin(i * degree) * rTL, yTL + cos(i * degree) * rTL)
                i += 1.0
            }
        }
        var i = 270.0
        while (i <= 360) {
            GL11.glVertex2d(xBL + sin(i * degree) * rBL, yBL + cos(i * degree) * rBL)
            i += 1.0
        }
        GL11.glEnd()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glPopMatrix()
    }
}