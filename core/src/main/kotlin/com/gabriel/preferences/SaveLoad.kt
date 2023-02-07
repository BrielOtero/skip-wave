package com.gabriel.preferences

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import com.gabriel.SkipWave.Companion.PREF_KEY_SAVE_STATE


data class Settings(
    var musicVolume: Float = 0.50f,
    var effectsVolume: Float = 0.75f,
)

data class Records(
    var wave: Int = 0,
)

data class GamePreferences(
    val settings: Settings = Settings(),
    val records: Records = Records(),
)


fun Preferences.saveGamePreferences(gamePreferences: GamePreferences) {
    this.flush {
        set(PREF_KEY_SAVE_STATE, Json().toJson(gamePreferences))
    }
}

fun Preferences.loadGamePreferences(): GamePreferences? {
    return Json().fromJson(GamePreferences::class.java, this[PREF_KEY_SAVE_STATE, "{}"])
}










