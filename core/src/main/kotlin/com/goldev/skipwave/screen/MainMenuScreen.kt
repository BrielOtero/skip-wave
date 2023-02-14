package com.goldev.skipwave.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.goldev.skipwave.SkipWave
import com.goldev.skipwave.SkipWave.Companion.ANIMATION_DURATION
import com.goldev.skipwave.preferences.saveGamePreferences
import com.goldev.skipwave.ui.model.MainMenuModel
import com.goldev.skipwave.ui.model.SettingsModel
import com.github.quillraven.fleks.world
import com.goldev.skipwave.event.*
import com.goldev.skipwave.system.AudioSystem
import com.goldev.skipwave.system.VibrateSystem
import com.goldev.skipwave.ui.view.mainMenuView
import com.goldev.skipwave.ui.view.settingsView
import ktx.app.KtxScreen
import ktx.log.logger
import ktx.scene2d.actors

class MainMenuScreen(private val game: SkipWave) : KtxScreen, EventListener {
    private val gameStage = game.gameStage
    private val uiStage = game.uiStage
    private val mainMenuModel = MainMenuModel(game.bundle,uiStage, gameStage)
    private val settingsModel = SettingsModel(game.bundle,game.gamePreferences, gameStage,uiStage)

    private val eWorld = world {

        injectables {
//            add("gameStage", gameStage)
//            add("uiStage", uiStage)
            add(game.gamePreferences)
        }

        systems {
            add<VibrateSystem>()
            add<AudioSystem>()
        }
    }

    init {
        Gdx.input.inputProcessor = uiStage
        uiStage.addListener(mainMenuModel)
        uiStage.addListener(settingsModel)
        uiStage.addListener(this)
        gameStage.addListener(this)
        uiStage.actors {
            mainMenuView(mainMenuModel)
            settingsView(settingsModel)
        }
        gameStage.root.addAction(Actions.fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))
        uiStage.root.addAction(Actions.fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))
    }




    override fun show() {
        log.debug { "MainMenuScreen gets shown" }
        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }
        gameStage.fire(ShowMainMenuViewEvent())
        uiStage.isDebugAll = true
    }

    override fun render(delta: Float) {
        uiStage.act()
        uiStage.draw()
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        eWorld.update(dt)
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

            is SavePreferencesEvent ->{
                log.debug { "SAVE GAME" }
                log.debug { "${game.gamePreferences.settings.musicVolume}" }
                game.preferences.saveGamePreferences(game.gamePreferences)
            }

            is ExitGameEvent ->{
                log.debug { "EXIT GAME" }
                Gdx.app.exit()
            }

            else -> return false
        }
        return true
    }

    override fun dispose() {
        super.dispose()
        eWorld.dispose()
    }

    companion object {
        private val log = logger<MainMenuScreen>()
    }
}