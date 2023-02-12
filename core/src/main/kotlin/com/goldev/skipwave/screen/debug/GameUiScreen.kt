package com.goldev.skipwave.screen.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.goldev.skipwave.SkipWave
import com.goldev.skipwave.component.LifeComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.ui.model.GameModel
import com.goldev.skipwave.ui.view.GameView
import com.goldev.skipwave.ui.view.gameView
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

class GameUiScreen(private val game: SkipWave) : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(180f, 320f))
    private val eWorld = world { }
    private val playerEntity: Entity
    private val model = GameModel(eWorld, game.bundle, stage, stage)
    private lateinit var gameView: GameView

    init {
        playerEntity = eWorld.entity {
            add<PlayerComponent>()
            add<LifeComponent> {
                max = 5f
                life = 3f
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            gameView = gameView(model)
        }
        stage.isDebugAll = true

    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            hide()
            show()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            gameView.playerLifeBar(0.5f)
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            gameView.playerLifeBar(1f)
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            gameView.playerExperience(0f)
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            gameView.playerExperience(1f)
        }



        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.disposeSafely()
    }
}