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

/**
 * The model of the Settings
 *
 * @property bundle The bundle with text to show in the UI.
 * @property gamePreferences The preferences of the game.
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor Create empty Settings model
 */
class SettingsModel(
    val bundle: I18NBundle,
    val gamePreferences: GamePreferences,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    /**
     *  Notifiable property with the music volume.
     */
    var musicVolume by propertyNotify(0)

    /**
     *  Notifiable property with the effects volume.
     */
    var effectsVolume by propertyNotify(0)

    /**
     *  Notifiable property with the main menu call.
     */
    var isMainMenuCall by propertyNotify(true)

    init {
        gameStage.addListener(this)
        uiStage.addListener(this)

    }

    /**
     * The function saves the settings to the preferences file and then fires an event to save the
     * preferences file
     */
    fun saveSettings() {
        gamePreferences.settings.musicVolume = musicVolume.toFloat() / 100
        gamePreferences.settings.effectsVolume = effectsVolume.toFloat() / 100
        gameStage.fire(SavePreferencesEvent())
        if (isMainMenuCall) {
            uiStage.actors.filterIsInstance<MainMenuView>().first().isVisible = true
        }
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
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

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SettingsModel>()
    }

}