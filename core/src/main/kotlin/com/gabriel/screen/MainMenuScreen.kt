package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.gabriel.SkipWave
import com.gabriel.SkipWave.Companion.ANIMATION_DURATION
import com.gabriel.event.*
import com.gabriel.ui.model.MainMenuModel
import com.gabriel.ui.view.*
import ktx.actors.alpha
import ktx.app.KtxScreen
import ktx.log.logger
import ktx.scene2d.actors

class MainMenuScreen(private val game: SkipWave) : KtxScreen, EventListener {
    private val gameStage = game.gameStage
    private val uiStage = game.uiStage
    private val model = MainMenuModel(game.bundle,uiStage, gameStage)

    init {
        Gdx.input.inputProcessor = uiStage
        uiStage.addListener(model)
        uiStage.addListener(this)
        uiStage.actors {
            mainMenuView(model)
        }
        gameStage.root.addAction(Actions.fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))
        uiStage.root.addAction(Actions.fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))
    }


    override fun show() {
        log.debug { "MainMenuScreen gets shown" }
//        uiStage.isDebugAll = true
    }

    override fun render(delta: Float) {
        uiStage.act()
        uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }


    override fun handle(event: Event): Boolean {

        when (event) {
            is SetGameScreenEvent -> {
                log.debug { "Set Screen" }

                gameStage.clear()
                uiStage.clear()
                game.addScreen(GameScreen(game))

//                game.gameStage.root.alpha=0f
//                game.uiStage.root.alpha=0f
                game.setScreen<GameScreen>()
                game.removeScreen<MainMenuScreen>()
                super.hide()
                dispose()
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<MainMenuScreen>()
    }
}