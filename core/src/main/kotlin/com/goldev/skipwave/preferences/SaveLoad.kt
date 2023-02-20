package com.goldev.skipwave.preferences

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import com.goldev.skipwave.SkipWave.Companion.PREF_KEY_SAVE_STATE


/**
 * It's a class with the data of game settings.
 *
 * @property musicVolume The volume of the music.
 * @property effectsVolume The volume of the sound effects.
 * @property vibrator If true, the vibrator will be used.
 * @property accelerometer If true, the accelerometer will be used.
 * @constructor Creates Settings with default values.
 */
data class Settings(
    var musicVolume: Float = 0.3f,
    var effectsVolume: Float = 1f,
    var vibrator: Boolean = true,
    var accelerometer: Boolean = true,
)

/**
 * It's a class with the data of game.
 *
 * @property wave The wave record.
 * @property tutorialComplete This is a boolean that will be used to determine if the player has completed the tutorial.
 * @constructor Creates Game with default values.
 */
data class Game(
    var wave: Int = 0,
    var tutorialComplete: Boolean = false,
)


/**
 * GamePreferences is a data class that contains the game preferences.
 *
 * @property settings The property with game Settings.
 * @property game The property with the game state.
 * @constructor Creates Game with default values.
 */
data class GamePreferences(
    val settings: Settings = Settings(),
    val game: Game = Game(),
)

/**
 * This function save the game preferences.
 *
 * @param gamePreferences Game preferences to save.
 */
fun Preferences.saveGamePreferences(gamePreferences: GamePreferences) {
    this.flush {
        set(PREF_KEY_SAVE_STATE, Json().toJson(gamePreferences))
    }
}

/**
 * This function load the game preferences.
 *
 * @return The load gamePreferences.
 */
fun Preferences.loadGamePreferences(): GamePreferences? {
    return try {
        Json().fromJson(GamePreferences::class.java, this[PREF_KEY_SAVE_STATE, "{}"])
    } catch (e: Exception) {
        GamePreferences()
    }
}










