package com.goldev.skipwave.preferences

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import com.goldev.skipwave.SkipWave.Companion.PREF_KEY_SAVE_STATE


data class Settings(
    var musicVolume: Float = 1f,
    var effectsVolume: Float = 1f,
    var vibrator: Boolean = true,
)

data class Game(
    var wave: Int = 0,
    var tutorialComplete: Boolean = false,
)

data class GamePreferences(
    val settings: Settings = Settings(),
    val game: Game = Game(),
)


fun Preferences.saveGamePreferences(gamePreferences: GamePreferences) {
    this.flush {
        set(PREF_KEY_SAVE_STATE, Json().toJson(gamePreferences))
    }
}

fun Preferences.loadGamePreferences(): GamePreferences? {
    try {
        return Json().fromJson(GamePreferences::class.java, this[PREF_KEY_SAVE_STATE, "{}"])
    } catch (e: Exception) {
        return GamePreferences()
    }
}










