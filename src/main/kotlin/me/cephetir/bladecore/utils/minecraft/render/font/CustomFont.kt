package me.cephetir.bladecore.utils.minecraft.render.font

import net.minecraft.client.renderer.texture.DynamicTexture
import org.lwjgl.opengl.GL11
import java.awt.*
import java.awt.image.BufferedImage


open class CustomFont(val font: Font) {
    var size = 4096
    var texture: DynamicTexture?
    private var height = 0
    protected var charData = arrayOfNulls<CharData>(512)
    var charOffset = 0

    init {
        texture = setupTexture(font, charData)
    }

    protected fun createFontImage(font: Font, chars: Array<CharData?>): BufferedImage {
        val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val g = image.graphics as Graphics2D
        g.font = font
        g.color = Color(255, 255, 255, 0)
        g.fillRect(0, 0, size, size)
        g.color = Color.WHITE
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val fontMetrics = g.fontMetrics
        var charHeight = 0
        var positionX = 0
        var positionY = 1
        for (i in chars.indices) {
            val ch = i.toChar()
            val charData = CharData()
            val dimensions = fontMetrics.getStringBounds(ch.toString(), g)
            charData.width = dimensions.bounds.width + 8
            charData.height = dimensions.bounds.height
            if (positionX + charData.width >= size) {
                positionX = 0
                positionY += charHeight
                charHeight = 0
            }
            if (charData.height > charHeight) {
                charHeight = charData.height
            }
            charData.storedX = positionX
            charData.storedY = positionY
            if (charData.height > height) {
                height = charData.height
            }
            chars[i] = charData
            g.drawString(ch.toString(), positionX + 2, positionY + fontMetrics.ascent)
            positionX += charData.width
        }
        return image
    }

    fun setupTexture(font: Font, chars: Array<CharData?>): DynamicTexture? {
        val img = createFontImage(font, chars)
        try {
            return DynamicTexture(img)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun drawChar(chars: Array<CharData?>, c: Char, x: Float, y: Float) {
        try {
            drawQuad(
                x,
                y,
                chars[c.code]!!.width.toFloat(),
                chars[c.code]!!.height.toFloat(),
                chars[c.code]!!.storedX.toFloat(),
                chars[c.code]!!.storedY.toFloat(),
                chars[c.code]!!.width.toFloat(),
                chars[c.code]!!.height.toFloat()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun drawQuad(x: Float, y: Float, width: Float, height: Float, srcX: Float, srcY: Float, srcWidth: Float, srcHeight: Float) {
        val renderSRCX = srcX / size
        val renderSRCY = srcY / size
        val renderSRCWidth = srcWidth / size
        val renderSRCHeight = srcHeight / size
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY)
        GL11.glVertex2d((x + width).toDouble(), y.toDouble())
        GL11.glTexCoord2f(renderSRCX, renderSRCY)
        GL11.glVertex2d(x.toDouble(), y.toDouble())
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight)
        GL11.glVertex2d(x.toDouble(), (y + height).toDouble())
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight)
        GL11.glVertex2d(x.toDouble(), (y + height).toDouble())
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight)
        GL11.glVertex2d((x + width).toDouble(), (y + height).toDouble())
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY)
        GL11.glVertex2d((x + width).toDouble(), y.toDouble())
    }

    fun getHeight(): Int {
        return (height - 8) / 2
    }

    open fun getStringWidth(text: String): Int {
        var width = 0
        for (c in text.toCharArray())
            if (c.code < charData.size && c.code >= 0)
                width += charData[c.code]!!.width - 8 + charOffset
        return width / 2
    }

    inner class CharData {
        var width = 0
        var height = 0
        var storedX = 0
        var storedY = 0
    }
}