package me.cephetir.bladecore.config.gui.comps.settings

interface SettingGui {
    fun draw(x: Float, y: Float, width: Float, mouseX: Int, mouseY: Int)
    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int)
    fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long)
    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int)
    fun keyTyped(typedChar: Char, keyCode: Int)
}