package com.gabriel.preferences

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

private class saveLoad() {
    private lateinit var preferences: Preferences

}


private data class GameRecords(
    var wave: Int = 0,
)

data class GamePreferences(
    var musicVolume: Float = 100f,
    var effectsVolume: Float = 100f,
)


fun loadPreferences() {
    var prefs: Preferences = Gdx.app.getPreferences("SkipWave")
//    prefs.get("musicVolume")

}