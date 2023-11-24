package me.cephetir.bladecore.utils.minecraft.render.font

import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Font


class CustomFontRenderer(font: Font) : CustomFont(font) {
    private val colorCode = IntArray(32)

    init {
        setupMinecraftColorCodes()
    }

    fun drawStringWithShadow(text: String, x: Double, y: Double, color: Color): Float {
        val shadowWidth = drawString(text, x - 1.0, y + 1.0, color, true)
        return shadowWidth.coerceAtLeast(drawString(text, x, y, color, false))
    }
    fun drawStringWithSlightShadow(text: String, x: Double, y: Double, color: Color): Float {
        val shadowWidth = drawString(text, x + 0.3, y + 1.0, color, true)
        return shadowWidth.coerceAtLeast(drawString(text, x, y, color, false))
    }

    fun drawString(text: String, d: Double, e: Double, color: Color): Float = drawString(text, d, e, color, false)
    fun drawCenteredStringWithShadow(text: String, x: Float, y: Float, color: Color): Float = drawStringWithShadow(text, (x - getStringWidth(text) / 2).toDouble(), y.toDouble(), color)
    fun drawCenteredStringWithSlightShadow(text: String, x: Float, y: Float, color: Color): Float = drawStringWithSlightShadow(text, (x - getStringWidth(text) / 2).toDouble(), y.toDouble(), color)
    fun drawCenteredString(text: String, x: Float, y: Float, color: Color): Float = drawString(text, (x - getStringWidth(text) / 2).toDouble(), y.toDouble(), color)

    fun drawString(text: String, x: Double, y: Double, gsColor: Color, shadow: Boolean): Float {
        if (text.isEmpty()) return 0f
        var x = (x - 1) * 2
        val y = (y - 2) * 2
        var color: Color = gsColor
        if (color.red == 255 && color.green == 255 && color.blue == 255 && color.alpha == 32) color = Color(255, 255, 255)
        if (color.alpha < 4) color = Color(color.red, color.blue, color.green, 255)
        if (shadow) color = Color(color.red / 4, color.green / 4, color.blue / 4, color.alpha)
        var currentData = charData
        var strikethrough = false
        var underline = false

        GlStateManager.pushMatrix()
        GlStateManager.scale(0.5, 0.5, 0.5)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture!!.glTextureId)

        val size = text.length
        var i = 0
        while (i < size) {
            val character = text[i]
            if (character == '\u00A7') {
                var colorIndex = "0123456789abcdefklmnor".indexOf(text[i + 1])
                if (colorIndex < 16) {
                    underline = false
                    strikethrough = false
                    GL11.glBindTexture(
                        GL11.GL_TEXTURE_2D,
                        texture!!.glTextureId
                    )
                    currentData = charData
                    if (colorIndex < 0) colorIndex = 15
                    if (shadow) colorIndex += 16
                    val colorcode = colorCode[colorIndex]
                    GlStateManager.color((colorcode shr 16 and 0xFF) / 255.0f, (colorcode shr 8 and 0xFF) / 255.0f, (colorcode and 0xFF) / 255.0f, color.alpha.toFloat())
                } else if (colorIndex == 18) {
                    strikethrough = true
                } else if (colorIndex == 19) {
                    underline = true
                } else if (colorIndex == 21) {
                    underline = false
                    strikethrough = false
                    GlStateManager.color(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, color.alpha / 255.0f)
                    GL11.glBindTexture(
                        GL11.GL_TEXTURE_2D,
                        texture!!.glTextureId
                    )
                    currentData = charData
                }
                i++
            } else if (character.code < currentData.size && character.code >= 0) {
                GL11.glBegin(GL11.GL_TRIANGLES)
                drawChar(currentData, character, x.toFloat(), y.toFloat())
                GL11.glEnd()
                if (strikethrough) drawLine(x, y + currentData[character.code]!!.height / 2, x + currentData[character.code]!!.width - 8.0, y + currentData[character.code]!!.height / 2, 1.0f)
                if (underline) drawLine(x, y + currentData[character.code]!!.height - 2.0, x + currentData[character.code]!!.width - 8.0, y + currentData[character.code]!!.height - 2.0, 1.0f)
                x += (currentData[character.code]!!.width - 8 + charOffset).toDouble()
            }
            i++
        }
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE)
        GlStateManager.popMatrix()

        return x.toFloat() / 2f
    }

    override fun getStringWidth(text: String): Int {
        var width = 0
        val currentData = charData
        val size = text.length
        var i = 0
        while (i < size) {
            val character = text[i]
            if (character == '\u00A7') i++
            else if (character.code < currentData.size && character.code >= 0)
                width += currentData[character.code]!!.width - 8 + charOffset
            i++
        }
        return width / 2
    }

    private fun drawLine(x: Double, y: Double, x1: Double, y1: Double, width: Float) {
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glLineWidth(width)
        GL11.glBegin(1)
        GL11.glVertex2d(x, y)
        GL11.glVertex2d(x1, y1)
        GL11.glEnd()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
    }

    fun wrapWords(text: String, width: Double): List<String> {
        val finalWords = mutableListOf<String>()

        if (getStringWidth(text) > width) {
            val words = text.trim().split(" ")
            var currentWord = ""
            var lastColorCode = 65535.toChar()
            for (word in words) {
                for (i in word.toCharArray().indices) {
                    val c = word.toCharArray()[i]
                    if (c == '\u00A7' && i < word.toCharArray().size - 1)
                        lastColorCode = word.toCharArray()[i + 1]
                }

                currentWord = if (getStringWidth("$currentWord$word ") < width) "$currentWord$word "
                else {
                    finalWords.add(currentWord)
                    "\u00A7$lastColorCode$word "
                }
            }

            if (currentWord.isNotEmpty())
                if (getStringWidth(currentWord) < width) finalWords.add("\u00A7$lastColorCode$currentWord ")
                else for (s in formatString(currentWord, width)) finalWords.add(s)
        } else finalWords.add(text)

        return finalWords
    }

    fun formatString(string: String, width: Double): List<String> {
        val finalWords = mutableListOf<String>()

        var currentWord = ""
        var lastColorCode = 65535.toChar()
        val chars = string.toCharArray()
        for (i in chars.indices) {
            val c = chars[i]
            if (c == '\u00A7' && i < chars.size - 1)
                lastColorCode = chars[i + 1]

            currentWord = if (getStringWidth(currentWord + c) < width) currentWord + c
            else {
                finalWords.add(currentWord)
                "\u00A7" + lastColorCode + c.toString()
            }
        }

        if (currentWord.isNotEmpty())
            finalWords.add(currentWord)

        return finalWords
    }

    private fun setupMinecraftColorCodes() {
        for (index in 0..31) {
            val noClue = (index shr 3 and 0x1) * 85
            var red = (index shr 2 and 0x1) * 170 + noClue
            var green = (index shr 1 and 0x1) * 170 + noClue
            var blue = (index shr 0 and 0x1) * 170 + noClue
            if (index == 6) red += 85
            if (index >= 16) {
                red /= 4
                green /= 4
                blue /= 4
            }
            colorCode[index] = red and 0xFF shl 16 or (green and 0xFF shl 8) or (blue and 0xFF)
        }
    }
}