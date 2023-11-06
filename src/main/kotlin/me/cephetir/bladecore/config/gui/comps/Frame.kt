package me.cephetir.bladecore.config.gui.comps

import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.SettingManager
import me.cephetir.bladecore.core.config.BladeConfig
import me.cephetir.bladecore.utils.ColorUtils.withAlpha
import me.cephetir.bladecore.utils.mc
import me.cephetir.bladecore.utils.minecraft.render.RenderUtils
import me.cephetir.bladecore.utils.minecraft.render.RoundUtils
import me.cephetir.bladecore.utils.minecraft.render.shaders.BlurUtils
import me.cephetir.bladecore.utils.minecraft.render.shaders.ShadowUtils
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import kotlin.math.max

class Frame(val gui: GuiScreen, private val settingManager: SettingManager) {
    companion object {
        val colorPrimary = Color(BladeConfig.primaryColorR.value.toInt(), BladeConfig.primaryColorG.value.toInt(), BladeConfig.primaryColorB.value.toInt())
        val colorSecondary = Color(191, 189, 193)
        val colorBG = Color(0, 0, 0, 175)
        val colorBGGlow = Color(17, 0, 25, 175)
    }

    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f

    private var scrollPixelsSettings = 0f
    private var scrollPixelsCategories = 0f
    private var lastScrollPixelsSettings = 0f
    private var lastScrollPixelsCategories = 0f
    private var stopScrollUp = false
    private var stopScrollUp2 = false

    private var categories = LinkedList<CategoryButton>()
    private var subCategories = LinkedList<SubCategoryGui>()
    var currentCategory: SettingManager.Category? = null
        private set
    val searchBar = SearchBar(this)

    init {
        val cats = settingManager.getCategories()
        for ((i, category) in cats.withIndex())
            categories.add(CategoryButton(this, category, i))
        setCurrCategory(null)
    }

    fun setCurrCategory(value: SettingManager.Category?) {
        currentCategory = value

        subCategories.clear()
        if (value != null) value.subCategories.forEach { subCategory ->
            subCategories.add(SubCategoryGui(this, subCategory))
        } else for (category in categories) category.category.subCategories.forEach { subCategory ->
            subCategories.add(SubCategoryGui(this, subCategory))
        }
        scrollPixelsSettings = 0f
        lastScrollPixelsSettings = 0f
    }

    fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val scaleFactor = RenderUtils.getScaleFactor()
        width = max(gui.width / scaleFactor / 2f, 150f)
        height = max(gui.height / scaleFactor / 1.5f, 112.5f)
        x = max(gui.width / scaleFactor / 2f - width / 2f, 0f)
        y = max(gui.height / scaleFactor / 2f - height / 2f, 0f)

        GlStateManager.pushMatrix()

        // Base
        if (BladeConfig.guiPostProcessing.value) ShadowUtils.shadow(
            12f,
            {
                RoundUtils.drawRoundedRect(
                    x * scaleFactor,
                    y * scaleFactor,
                    (x + width) * scaleFactor,
                    (y + height) * scaleFactor,
                    5f,
                    colorBG.withAlpha(255).rgb
                )
            },
            {
                RoundUtils.drawRoundedRect(
                    x * scaleFactor,
                    y * scaleFactor,
                    (x + width) * scaleFactor,
                    (y + height) * scaleFactor,
                    5f
                )
            }
        )
        if (BladeConfig.guiPostProcessing.value)
            BlurUtils.blurAreaRounded(
                x * scaleFactor,
                y * scaleFactor,
                (x + width) * scaleFactor,
                (y + height) * scaleFactor,
                5f,
                10f
            )
        GlStateManager.scale(scaleFactor.toDouble(), scaleFactor.toDouble(), scaleFactor.toDouble())
        RoundUtils.drawSmoothRoundedRect(x, y, x + width, y + height, 4f, colorBG.rgb)
        // Right sidebar
        RoundUtils.customRounded(
            x + width / 4f,
            y,
            x + width,
            y + height,
            0f, 2.75f, // idk why its 2.75 but it matches the 4 radius of smooth rect ^
            2.75f, 0f,
            colorBGGlow.rgb
        )
        RenderUtils.drawRect(
            x + width / 4f,
            y + 0.3f,
            x + width / 4f + 0.5f,
            y + height - 0.3f,
            colorPrimary
        )
        RenderUtils.drawHorizontalGradientRect(
            x + width / 4f - 1.5f,
            y + 0.3f,
            x + width / 4f,
            y + height - 0.3f,
            colorPrimary.withAlpha(0).rgb,
            colorPrimary.rgb
        )

        // Main
        GlStateManager.scale(0.5, 0.5, 0.5)
        if (settingManager.name.isNotEmpty())
            ConfigGui.fontRenderer32.drawStringWithShadow(
                settingManager.name,
                (x + 3) * 2.0,
                (y - 2 - ConfigGui.fontRenderer32.getHeight() / 2f) * 2.0,
                colorPrimary
            )

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        lastScrollPixelsCategories = RenderUtils.animate(scrollPixelsCategories, lastScrollPixelsCategories, 0.3f)
        for (category in categories) {
            RenderUtils.scissor(x * 2f, y * 2f, (x + width / 4f) * 2f, (y + height) * 2f)
            category.draw(mouseX, mouseY, lastScrollPixelsCategories)
        }
        RenderUtils.clearScissors()
        stopScrollUp2 = categories.last.calcY(lastScrollPixelsCategories) < y + height
        GL11.glDisable(GL11.GL_SCISSOR_TEST)

        // Title
        if (currentCategory != null) ConfigGui.fontRenderer32.drawString(
            currentCategory!!.name,
            (x + width * 5f / 8f - ConfigGui.fontRenderer32.getStringWidth(currentCategory!!.name) / 2f / 2f) * 2.0,
            (y + 8.5f - ConfigGui.fontRenderer32.getHeight() / 2f / 2f) * 2.0,
            colorPrimary
        )
        RenderUtils.drawRect(
            (x + width / 4f) * 2f,
            (y + 17) * 2f,
            (x + width) * 2f,
            (y + 17.4f) * 2f,
            colorPrimary
        )
        RenderUtils.drawVerticalGradientRect(
            (x + width / 4f + 0.5f) * 2f,
            (y + 17.4f) * 2f,
            (x + width) * 2f,
            (y + 20f) * 2f,
            colorPrimary.rgb,
            colorPrimary.withAlpha(0).rgb
        )

        run {
            val x = (x + width / 4f) * 2f
            val y = (y + 2.5f) * 2f
            val width = (width - width / 4f) * 2f
            searchBar.draw(x, y, width, mouseX, mouseY)
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        RenderUtils.scissor((x + width / 4f) * 2f, (y + 20f) * 2f, (x + width) * 2f, (y + height - 2f) * 2f)
        lastScrollPixelsSettings = RenderUtils.animate(scrollPixelsSettings, lastScrollPixelsSettings, 0.3f)
        var h = lastScrollPixelsSettings
        for (subCategory in subCategories) {
            val add = subCategory.draw(mouseX, mouseY, h, searchBar.value)
            if (add != 0f) {
                h += add + 5f
                if ((y + 20f) * 2 + h > (y + height - 2f) * 2) break
            }
        }
        stopScrollUp = (y + 20f) * 2 + h < (y + height - 2f) * 2
        GL11.glDisable(GL11.GL_SCISSOR_TEST)

        GlStateManager.scale(2.0, 2.0, 2.0)
        GlStateManager.popMatrix()
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        for (category in categories)
            category.mouseClicked(mouseX, mouseY, mouseButton)
        for (subCategory in subCategories)
            subCategory.mouseClicked(mouseX, mouseY, mouseButton)
        searchBar.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        for (subCategory in subCategories)
            subCategory.mouseReleased(mouseX, mouseY, state)
    }

    fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        for (subCategory in subCategories)
            subCategory.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        for (subCategory in subCategories)
            subCategory.keyTyped(typedChar, keyCode)
        searchBar.keyTyped(typedChar, keyCode)
    }

    fun handleMouseInput() {
        val scrollDelta = Mouse.getEventDWheel()
        if (scrollDelta == 0) return

        val mX = Mouse.getEventX() * width / mc.displayWidth
        val mY = height - Mouse.getEventY() * height / mc.displayHeight - 1
        if (mX > (x + width / 4f) / 2f && mY > y / 2f + 20f && mX < (x + width) / 2f && mY < y / 2f + height - 2f) {
            if (!stopScrollUp || scrollDelta > 0)
                scrollPixelsSettings = (scrollPixelsSettings + scrollDelta * 0.2f).coerceAtMost(0f)
        } else if (mX > x / 2f && mY > y / 2f && mX < (x + width / 4f) / 2f && mY < y / 2f + height) {
            if (!stopScrollUp2 || scrollDelta > 0)
                scrollPixelsCategories = (scrollPixelsCategories + scrollDelta * 0.2f).coerceAtMost(0f)
        }
    }
}