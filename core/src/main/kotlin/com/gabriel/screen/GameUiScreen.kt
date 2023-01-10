package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.component.LifeComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.event.EntityDamageEvent
import com.gabriel.event.fire
import com.gabriel.ui.Drawables
import com.gabriel.ui.model.GameModel
import com.gabriel.ui.view.GameView
import com.gabriel.ui.view.HUDView
import com.gabriel.ui.view.gameView
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

class GameUiScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(320f, 180f))
    private val eWorld = world { }
    private val playerEntity: Entity
    private val model = GameModel(eWorld, stage)
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
            gameView.playerLife(0.5f)
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            gameView.playerLife(1f)
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            gameView.popup("You found something [#ff0000]cool[]!")
        }



        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.disposeSafely()
    }
}