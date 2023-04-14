package me.cephetir.bladecore.utils

import java.awt.Color

object ColorUtils {
    fun Color.withAlpha(alpha: Int): Color = Color(this.red, this.green, this.blue, alpha.coerceIn(0, 255))

    fun Color.toHex(): String = "#" + Integer.toHexString(this.rgb)

    fun String.fromHex(): Color? {
        val hex = this.replace("#", "")
        when (hex.length) {
            6 -> return Color(
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16),
            )
            8 -> return Color(
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16),
                Integer.valueOf(hex.substring(6, 8), 16),
                Integer.valueOf(hex.substring(0, 2), 16),
            )
        }
        return null
    }

    fun blendColors(fractions: FloatArray, colors: Array<Color>, progress: Float): Color =
        if (fractions.size == colors.size) {
            val indices = getFractionIndices(fractions, progress)
            val range = floatArrayOf(fractions[indices[0]], fractions[indices[1]])
            val colorRange = arrayOf(colors[indices[0]], colors[indices[1]])
            val max = range[1] - range[0]
            val value = progress - range[0]
            val weight = value / max
            blend(colorRange[0], colorRange[1], (1.0f - weight).toDouble())
        } else throw IllegalArgumentException("Fractions and colours must have equal number of elements")


    private fun getFractionIndices(fractions: FloatArray, progress: Float): IntArray {
        val range = IntArray(2)
        var startPoint = 0
        while (startPoint < fractions.size && fractions[startPoint] <= progress) ++startPoint
        if (startPoint >= fractions.size) startPoint = fractions.size - 1
        range[0] = startPoint - 1
        range[1] = startPoint
        return range
    }

    private fun blend(color1: Color, color2: Color, ratio: Double): Color {
        val r = ratio.toFloat()
        val ir = 1.0f - r
        val rgb1 = color1.getColorComponents(FloatArray(3))
        val rgb2 = color2.getColorComponents(FloatArray(3))
        val red = (rgb1[0] * r + rgb2[0] * ir).coerceIn(0f, 1f)
        val green = (rgb1[1] * r + rgb2[1] * ir).coerceIn(0f, 1f)
        val blue = (rgb1[2] * r + rgb2[2] * ir).coerceIn(0f, 1f)
        return Color(red, green, blue)
    }
}