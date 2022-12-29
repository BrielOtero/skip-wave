package com.gabriel

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.gabriel.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Survivor : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG;
        addScreen(GameScreen());
        setScreen<GameScreen>();
    }
}
