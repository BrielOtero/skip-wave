package com.gabriel

import com.badlogic.gdx.Application.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.event.GamePauseEvent
import com.gabriel.event.GameResumeEvent
import com.gabriel.event.TestEvent
import com.gabriel.screen.TouchpadScreen
import com.gabriel.screen.GameScreen
import com.gabriel.screen.GameUiScreen
import com.gabriel.screen.SkillUpgradeScreen
//import com.gabriel.screen.GameUiScreen
//import com.gabriel.screen.InventoryUiScreen
import com.gabriel.ui.disposeSkin
import com.gabriel.ui.loadSkin
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Survivor : KtxGame<KtxScreen>() {
    private val batch: Batch by lazy { SpriteBatch() }
    private lateinit var gameViewport: ExtendViewport
    private lateinit var uiViewport: ExtendViewport
    val gameStage: Stage by lazy { Stage(gameViewport) }
    val uiStage: Stage by lazy { Stage(uiViewport) }


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

        loadSkin()

//        gameStage.addListener(this)

        addScreen(GameScreen(this))
//        addScreen(GameUiScreen())
//        addScreen(TouchpadScreen(uiViewport))
        addScreen(SkillUpgradeScreen())
        setScreen<GameScreen>()
//        setScreen<GameUiScreen>()
//        setScreen<TouchpadScreen>()
//        setScreen<SkillUpgradeScreen>()
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

//    override fun handle(event: Event): Boolean {
//        when(event){
//            is GamePauseEvent ->{
//                currentScreen.pause()
//                log.debug { "pause" }
//
//            }
//            is GameResumeEvent ->{
//                currentScreen.resume()
//                log.debug { "resume" }
//            }
//            is TestEvent ->{
//                log.debug { "TESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" }
//            }
//            else -> return false
//        }
//        return true
//    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
        private var log = logger<Survivor>()
    }
}
