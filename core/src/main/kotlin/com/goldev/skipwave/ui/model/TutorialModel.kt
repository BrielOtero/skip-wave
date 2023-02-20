package com.goldev.skipwave.ui.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.event.*
import com.goldev.skipwave.preferences.GamePreferences
import com.goldev.skipwave.ui.view.*
import ktx.actors.alpha
import ktx.log.logger

/**
 * The model of the Tutorial
 *
 * @property bundle The bundle with text to show in the UI.
 * @property gamePreferences The preferences of the game.
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor Create empty Tutorial model
 */
class TutorialModel(
    val bundle: I18NBundle,
    val gamePreferences: GamePreferences,
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
            is ShowTutorialViewEvent -> {
                gameStage.fire(GamePauseEvent())
                uiStage.actors.filterIsInstance<TutorialView>().first().isVisible = true
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                    this.model.disableTouchpad = true
                    isVisible = false
                }

                with(uiStage.actors.filterIsInstance<GameView>().first()) {
                    btnPause.touchable = Touchable.disabled
                    btnPause.isVisible = false
                    showBackground(true)
                }
            }

            is HideTutorialViewEvent -> {
                uiStage.actors.filterIsInstance<TutorialView>().first().isVisible = false
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                    this.model.disableTouchpad = false
                    isVisible = true
                }

                uiStage.actors.filterIsInstance<TouchpadView>().first().touchable =
                    Touchable.enabled

                with(uiStage.actors.filterIsInstance<GameView>().first()) {
                    btnPause.touchable = Touchable.enabled
                    btnPause.isVisible = true
                    showBackground(false)
                }
                gamePreferences.game.tutorialComplete = true
                gameStage.fire(SavePreferencesEvent())
                gameStage.fire(GameResumeEvent())
                uiStage.actors.filterIsInstance<TutorialView>().first().remove()


            }

            else -> return false
        }
        return true
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<TutorialModel>()
    }
}

