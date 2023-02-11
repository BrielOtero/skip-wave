package com.gabriel.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.gabriel.event.*
import com.gabriel.ui.view.GameView
import com.gabriel.ui.view.PauseView
import com.github.quillraven.fleks.Qualifier
import ktx.log.logger

class PauseModel(
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    init {
        gameStage.addListener(this)
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is ShowPauseViewEvent -> {
                gameStage.fire(GamePauseEvent())
                uiStage.actors.filterIsInstance<PauseView>().first().isVisible = true
                uiStage.actors.filterIsInstance<GameView>().first().isVisible = false
            }

            is HidePauseViewEvent -> {
                uiStage.actors.filterIsInstance<PauseView>().first().isVisible = false
                uiStage.actors.filterIsInstance<GameView>().first().isVisible = true
                gameStage.fire(GameResumeEvent())
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<PauseModel>()
    }
}
