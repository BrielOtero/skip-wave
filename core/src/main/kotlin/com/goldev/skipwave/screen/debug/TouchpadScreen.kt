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

/**
 *  It's a screen for test TouchpadView.
 */
class TouchpadScreen : KtxScreen {
    /**
     *  The property with a stage to show.
     */
    private val stage: Stage = Stage(ExtendViewport(180f, 320f))
    /**
     *  Property with world for entities.
     */
    private val eWorld = world { }
    /**
     *  Property that contains player entity.
     */
    private val playerEntity: Entity
    /**
     *  Property with model for the Touchpad.
     */
    private val model = TouchpadModel(eWorld, stage)

    init {
        playerEntity = eWorld.entity {
            add<PlayerComponent>()
            add<MoveComponent>()
        }
    }

    /**
     * When the screen is resized, update the viewport to the new width and height.
     *
     * @param width The width of the screen in pixels.
     * @param height The height of the screen in pixels.
     */
    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    /**
     * This function is called when this TouchpadScreen appears.
     */
    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            touchpadView(TouchpadModel(eWorld, stage))
        }
        PlayerTouchInputProcessor(stage)
        stage.isDebugAll = true
        Gdx.input.inputProcessor = stage

    }

    /**
     * The render function of the screen. It is called every frame.
     *
     * @param delta The time in seconds since the last frame.
     */
    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            hide()
            show()
        }

        stage.act()
        stage.draw()
    }

    /**
     * It disposes all resources when TouchpadScreen is closed.
     */
    override fun dispose() {
        stage.disposeSafely()
    }
}