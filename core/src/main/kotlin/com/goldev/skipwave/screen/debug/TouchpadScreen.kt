package com.goldev.skipwave.screen.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.goldev.skipwave.component.MoveComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.input.PlayerTouchInputProcessor
import com.goldev.skipwave.ui.model.TouchpadModel
import com.goldev.skipwave.ui.view.TouchpadView
import com.goldev.skipwave.ui.view.touchpadView
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

class TouchpadScreen(
    val viewport: ExtendViewport
) : KtxScreen {
    private val eWorld = world { }
    private val playerEntity: Entity
    val stage: Stage = Stage(viewport)
    private val model = TouchpadModel(eWorld, stage)
    private lateinit var hudView: TouchpadView

    init {
        playerEntity = eWorld.entity {
            add<PlayerComponent>()
            add<MoveComponent>()
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            touchpadView(TouchpadModel(eWorld, stage))
        }
        PlayerTouchInputProcessor(eWorld, stage)
        stage.isDebugAll = true
        Gdx.input.inputProcessor = stage

    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            hide()
            show()
        } else if (Gdx.input.isTouched) {

        }

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.disposeSafely()
    }
}