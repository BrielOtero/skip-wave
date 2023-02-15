package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.event.SavePreferencesEvent
import com.goldev.skipwave.event.ShowSettingsViewEvent
import com.goldev.skipwave.event.fire
import com.goldev.skipwave.preferences.GamePreferences
import com.goldev.skipwave.ui.view.MainMenuView
import com.goldev.skipwave.ui.view.SettingsView
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger

class SettingsModel(
    val bundle: I18NBundle,
    val gamePreferences: GamePreferences,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    var musicVolume by propertyNotify(0)
    var effectsVolume by propertyNotify(0)
    var isMainMenuCall by propertyNotify(true)

    init {
        gameStage.addListener(this)
        uiStage.addListener(this)

    }

    fun saveSettings() {
        gamePreferences.settings.musicVolume = musicVolume.toFloat() / 100
        gamePreferences.settings.effectsVolume = effectsVolume.toFloat() / 100
        gameStage.fire(SavePreferencesEvent())
        if (isMainMenuCall) {
            uiStage.actors.filterIsInstance<MainMenuView>().first().isVisible = true
        }
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is ShowSettingsViewEvent -> {
                log.debug { "EVENT: ShowSettingsEvent" }

                isMainMenuCall = event.isMainMenuCall
                musicVolume = (gamePreferences.settings.musicVolume * 100).toInt()
                effectsVolume = (gamePreferences.settings.effectsVolume * 100).toInt()

                uiStage.actors.filterIsInstance<SettingsView>().first().isVisible = true
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<SettingsModel>()
    }

}