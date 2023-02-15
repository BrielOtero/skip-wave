package com.goldev.skipwave

//import com.goldev.skipwave.screen.GameUiScreen
//import com.goldev.skipwave.screen.InventoryUiScreen
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


/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class SkipWave : KtxGame<KtxScreen>(){
    private val batch: Batch by lazy { SpriteBatch() }
    private lateinit var gameViewport: ExtendViewport
    private lateinit var uiViewport: ExtendViewport
    lateinit var bundle: I18NBundle
    val gameStage: Stage by lazy { Stage(gameViewport) }
    val uiStage: Stage by lazy { Stage(uiViewport) }
    val preferences: Preferences by lazy { Gdx.app.getPreferences(PREF_NAME) }
    lateinit var gamePreferences: GamePreferences

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

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    override fun dispose() {
        super.dispose()
        gameStage.disposeSafely()
        uiStage.disposeSafely()
        batch.disposeSafely()
        disposeSkin()
    }


    companion object {
        const val UNIT_SCALE = 1 / 16f // 16 pixels is one in game world unit
        const val PREF_NAME = "skip-wave"
        const val PREF_KEY_SAVE_STATE = "save-state"
        const val ANIMATION_DURATION = 0.3f
        private var log = logger<SkipWave>()
    }

}
