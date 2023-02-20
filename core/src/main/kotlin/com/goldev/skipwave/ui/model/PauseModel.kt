package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.view.GameView
import com.goldev.skipwave.ui.view.PauseView
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.view.TouchpadView
import ktx.log.logger

/**
 * The model of the Pause
 *
 * @property bundle The bundle with text to show in the UI.
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor Create empty Pause model
 */
class PauseModel(
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    init {
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
            is ShowPauseViewEvent -> {
                gameStage.fire(GamePauseEvent())
                uiStage.actors.filterIsInstance<PauseView>().first().isVisible = true
                uiStage.actors.filterIsInstance<GameView>().first().isVisible = false
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                    this.model.disableTouchpad = true
                    isVisible = false
                }
            }

            is HidePauseViewEvent -> {
                uiStage.actors.filterIsInstance<PauseView>().first().isVisible = false
                uiStage.actors.filterIsInstance<GameView>().first().isVisible = true
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                    this.model.disableTouchpad = false
                    isVisible = true
                }
                gameStage.fire(GameResumeEvent())
            }

            else -> return false
        }
        return true
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<PauseModel>()
    }
}
