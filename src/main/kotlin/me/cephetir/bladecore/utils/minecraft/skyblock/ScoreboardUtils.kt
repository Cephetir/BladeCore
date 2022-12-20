package me.cephetir.bladecore.utils.minecraft.skyblock

import me.cephetir.bladecore.utils.TextUtils.stripColor
import me.cephetir.bladecore.utils.world
import net.minecraft.scoreboard.Score
import net.minecraft.scoreboard.ScorePlayerTeam

object ScoreboardUtils {
    fun String.cleanSB(): String = this.stripColor().toCharArray().filter { it.code in 21..126 }.joinToString("")

    val sidebarLines: List<String>
        get() {
            val scoreboard = world?.scoreboard ?: return emptyList()
            val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return emptyList()
            val scores = scoreboard.getSortedScores(objective).filter { input: Score? ->
                input != null && input.playerName != null && !input.playerName.startsWith("#")
            }.take(15)
            return scores.map {
                ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(it.playerName), it.playerName)
            }
        }
}