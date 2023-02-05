package com.gabriel.screen.Debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.SkipWave
import com.gabriel.component.LifeComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.ui.model.GameModel
import com.gabriel.ui.model.RecordsModel
import com.gabriel.ui.view.GameView
import com.gabriel.ui.view.RecordsView
import com.gabriel.ui.view.gameView
import com.gabriel.ui.view.recordsView
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

class RecordsScreen(private val game: SkipWave) : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(180f, 320f))
    private val eWorld = world { }
    private val playerEntity: Entity
    private val model = RecordsModel(eWorld, game.bundle, game.gamePreferences, stage, stage)
    private lateinit var recordView: RecordsView

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
            recordView = recordsView(model)
        }
        stage.isDebugAll = true

    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            hide()
            show()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
        }

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.disposeSafely()
    }
}