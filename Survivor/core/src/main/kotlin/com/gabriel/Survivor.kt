package com.gabriel

import com.badlogic.gdx.Application.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.screen.GameScreen
import com.gabriel.screen.UiScreen
import com.gabriel.ui.disposeSkin
import com.gabriel.ui.loadSkin
import ktx.app.KtxGame
import ktx.app.KtxScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Survivor : KtxGame<KtxScreen>() {
    private lateinit var gameViewport: ExtendViewport
    private lateinit var uiViewport: ExtendViewport

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        when (Gdx.app.type) {
            ApplicationType.Desktop -> {
                gameViewport = ExtendViewport(16f, 9f)
                uiViewport = ExtendViewport(180f, 320f)
            }

            else -> {
                gameViewport = ExtendViewport(9f, 16f)
                uiViewport = ExtendViewport(180f, 320f)
            }
        }

        loadSkin()

        addScreen(GameScreen(gameViewport, uiViewport))
        addScreen(UiScreen())
        setScreen<GameScreen>()
//        setScreen<UiScreen>()
    }

    override fun dispose() {
        super.dispose()
        disposeSkin()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}
