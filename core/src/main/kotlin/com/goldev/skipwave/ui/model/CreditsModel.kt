package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.event.ShowCreditsViewEvent
import com.goldev.skipwave.event.ShowSettingsViewEvent
import com.goldev.skipwave.ui.view.CreditsView
import com.goldev.skipwave.ui.view.SettingsView
import ktx.log.logger


/**
 * The model of the Credits
 *
 * @property bundle The bundle with text to show in the UI.
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor Create empty Credits model.
 */
class CreditsModel(
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    init {
        uiStage.addListener(this)
        gameStage.addListener(this)
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
        when (event) {
            is ShowCreditsViewEvent -> {
                log.debug { "EVENT: ShowCreditsEvent" }

                uiStage.actors.filterIsInstance<CreditsView>().first().isVisible = true
            }
            else -> return false
        }
        return true
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SkillUpgradeModel>()
    }
}

