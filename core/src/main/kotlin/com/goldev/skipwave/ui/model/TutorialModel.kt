package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.event.*
import com.goldev.skipwave.ui.view.GameView
import com.goldev.skipwave.ui.view.PauseView
import com.goldev.skipwave.ui.view.TouchpadView
import com.goldev.skipwave.ui.view.TutorialView
import ktx.log.logger

class TutorialModel(
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    init {
        uiStage.addListener(this)
        gameStage.addListener(this)

    }

    override fun handle(event: Event): Boolean {

        when (event) {
            is ShowTutorialViewEvent -> {
                gameStage.fire(GamePauseEvent())
                uiStage.actors.filterIsInstance<TutorialView>().first().isVisible = true
//                uiStage.actors.filterIsInstance<GameView>().first().isVisible = false
                with(uiStage.actors.filterIsInstance<TouchpadView>().first()) {
                    this.model.disableTouchpad = true
                    isVisible = false
                }
            }

            is HideTutorialViewEvent -> {
                uiStage.actors.filterIsInstance<TutorialView>().first().isVisible = false
//                uiStage.actors.filterIsInstance<GameView>().first().isVisible = true
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
        private val log = logger<SkillUpgradeModel>()
    }
}

