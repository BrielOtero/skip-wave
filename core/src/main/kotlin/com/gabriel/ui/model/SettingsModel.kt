package com.gabriel.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.gabriel.event.SavePreferencesEvent
import com.gabriel.event.ShowSettingsViewEvent
import com.gabriel.event.fire
import com.gabriel.preferences.GamePreferences
import com.gabriel.ui.view.MainMenuView
import com.gabriel.ui.view.SettingsView
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger

class SettingsModel(
    val bundle: I18NBundle,
    val gamePreferences: GamePreferences,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    var musicVolume by propertyNotify(gamePreferences.settings.musicVolume * 100)
    var effectsVolume by propertyNotify(gamePreferences.settings.effectsVolume * 100)
    var isMainMenuCall by propertyNotify(true)

    init {
        gameStage.addListener(this)
    }

    fun saveSettings() {
        gamePreferences.settings.musicVolume = musicVolume / 100
        gamePreferences.settings.effectsVolume = effectsVolume / 100
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
                musicVolume = (gamePreferences.settings.musicVolume * 100)
                effectsVolume = (gamePreferences.settings.effectsVolume * 100)

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