package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.gabriel.SkipWave
import com.gabriel.event.*
import com.gabriel.ui.model.MainMenuModel
import com.gabriel.ui.view.*
import ktx.app.KtxScreen
import ktx.log.logger
import ktx.scene2d.actors

class MainMenuScreen(private val game: SkipWave) : KtxScreen, EventListener {
    private val gameStage = game.gameStage
    private val uiStage = game.uiStage
    private val model = MainMenuModel(uiStage, gameStage)

    init {
        Gdx.input.inputProcessor = uiStage
        uiStage.addListener(model)
        uiStage.addListener(this)
        uiStage.actors {
            mainMenuView(model)
        }
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
//                if (game.containsScreen<GameScreen>()) {
//                    game.removeScreen<GameScreen>()
//                }
                gameStage.clear()
                uiStage.clear()
                game.addScreen(GameScreen(game))
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