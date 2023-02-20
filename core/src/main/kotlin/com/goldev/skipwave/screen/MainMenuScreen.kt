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
import com.goldev.skipwave.ui.model.CreditsModel
import com.goldev.skipwave.ui.view.creditsView
import com.goldev.skipwave.ui.view.mainMenuView
import com.goldev.skipwave.ui.view.settingsView
import ktx.app.KtxScreen
import ktx.log.logger
import ktx.scene2d.actors

/**
 *  It's a screen that contains the main menu
 *
 *  @property game The property with game data.
 *  @constructor Creates MainMenuScreen
 */
class MainMenuScreen(private val game: SkipWave) : KtxScreen, EventListener {

    /**
     *  It's a property with stage that the game is being rendered on.
     */
    private val gameStage = game.gameStage

    /**
     *  It's a property with stage that the UI is being rendered on.
     */
    private val uiStage = game.uiStage

    /**
     *  It's a property with the entities world.
     */
    private val eWorld = world {

        /**
         * Add into the system common variables.
         */
        injectables {
            add(game.gamePreferences)
        }

        /**
         * Add into the system the listening systems that the game need.
         */
        systems {
            add<VibrateSystem>()
            add<AudioSystem>()
        }
    }

    init {
        Gdx.input.inputProcessor = uiStage
        uiStage.addListener(this)
        gameStage.addListener(this)
        uiStage.actors {
            mainMenuView(MainMenuModel(game.bundle, uiStage, gameStage))
            settingsView(SettingsModel(game.bundle, game.gamePreferences, gameStage, uiStage))
            creditsView(CreditsModel(game.bundle, gameStage, uiStage))
        }

    }


    /**
     * This function is called when this MainMenuScreen appears.
     */
    override fun show() {
        gameStage.root.addAction(Actions.fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))
        uiStage.root.addAction(Actions.fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))

        log.debug { "MainMenuScreen gets shown" }
        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }
        gameStage.fire(ShowMainMenuViewEvent())
//        uiStage.isDebugAll = true
    }

    /**
     * Update the timepiece, then update the world.
     *
     * @param delta The time in seconds since the last frame.
     */
    override fun render(delta: Float) {
        uiStage.act()
        uiStage.draw()
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        eWorld.update(dt)
    }

    /**
     * When the screen is resized, update the viewport of the gameStage and uiStage to the new width
     * and height.
     *
     * @param width The new width in pixels of the screen.
     * @param height The height of the screen in pixels.
     */
    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    /**
     * It disposes all resources when MainMenuScreen is closed.
     */
    override fun dispose() {
        super.dispose()
        eWorld.dispose()
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {

        when (event) {
            is SetGameScreenEvent -> {
                log.debug { "Set Screen" }

                gameStage.clear()
                uiStage.clear()
                game.addScreen(GameScreen(game))

                game.setScreen<GameScreen>()
                game.removeScreen<MainMenuScreen>()
                super.hide()
                dispose()
            }

            is SavePreferencesEvent -> {
                log.debug { "SAVE GAME" }
                log.debug { "${game.gamePreferences.settings.musicVolume}" }
                game.preferences.saveGamePreferences(game.gamePreferences)
            }

            is ExitGameEvent -> {
                log.debug { "EXIT GAME" }
                Gdx.app.exit()
            }

            else -> return false
        }
        return true
    }


    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<MainMenuScreen>()
    }
}