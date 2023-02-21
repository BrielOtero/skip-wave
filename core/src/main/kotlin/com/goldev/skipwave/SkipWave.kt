package com.goldev.skipwave

import com.badlogic.gdx.Application.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.goldev.skipwave.preferences.GamePreferences
import com.goldev.skipwave.preferences.loadGamePreferences
import com.goldev.skipwave.screen.MainMenuScreen
import com.goldev.skipwave.ui.disposeSkin
import com.goldev.skipwave.ui.loadSkin
import ktx.actors.alpha
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger
import java.util.Locale


/**
 * The game starts with the variables
 *
 * @constructor Create empty Skip wave
 */
class SkipWave : KtxGame<KtxScreen>() {
    /**
     *  It's a property with the batch of the game
     */
    private val batch: Batch by lazy { SpriteBatch() }

    /**
     *  It's a property with the viewport of the game.
     */
    private lateinit var gameViewport: ExtendViewport

    /**
     * It's a property with the viewport of the ui.
     */
    private lateinit var uiViewport: ExtendViewport

    /**
     * It's a property with the bundle with languages.
     */
    lateinit var bundle: I18NBundle

    /**
     *  It's a property with the stage that the game is being rendered on.
     */
    val gameStage: Stage by lazy { Stage(gameViewport) }

    /**
     *  It's a property with the stage that the UI is being rendered on.
     */
    val uiStage: Stage by lazy { Stage(uiViewport) }

    /**
     * Preferences of the game
     */
    val preferences: Preferences by lazy { Gdx.app.getPreferences(PREF_NAME) }

    /**
     * The preferences of the game.
     */
    lateinit var gamePreferences: GamePreferences

    /**
     * Initialize variables and set MenuScreen
     */
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        when (Gdx.app.type) {
            ApplicationType.Desktop -> {
//                gameViewport = ExtendViewport(16f, 9f,)
//                uiViewport = ExtendViewport(320f, 180f)
                gameViewport = ExtendViewport(8f, 16f)
                uiViewport = ExtendViewport(180f, 320f)
            }

            else -> {
                gameViewport = ExtendViewport(8f, 16f)
                uiViewport = ExtendViewport(180f, 320f)
            }
        }
        gameStage.root.alpha = 0f
        uiStage.root.alpha = 0f
        gamePreferences = preferences.loadGamePreferences()!!

        loadSkin()
        log.debug { "LOCALE ${Locale.getDefault().displayName}" }
        bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/MyBundle"))

        addScreen(MainMenuScreen(this))
//        addScreen(RecordsScreen(this))
//        addScreen(GameUiScreen(this))
        setScreen<MainMenuScreen>()
//        setScreen<RecordsScreen>()
//        setScreen<GameUiScreen>()
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
     * It disposes all resources when Game is closed.
     */
    override fun dispose() {
        super.dispose()
        gameStage.disposeSafely()
        uiStage.disposeSafely()
        batch.disposeSafely()
        disposeSkin()
    }

    companion object {

        /**
         * The game word unit scale
         */
        const val UNIT_SCALE = 1 / 16f // 16 pixels is one in game world unit

        /**
         * Preferred Name
         */
        const val PREF_NAME = "skip-wave"

        /**
         * Preferred Key Save State
         */
        const val PREF_KEY_SAVE_STATE = "save-state"

        /**
         * The animation duration when there a transition
         */
        const val ANIMATION_DURATION = 0.3f

        /**
         *  It's a logger that logs the class.
         */
        private var log = logger<SkipWave>()
    }

}
