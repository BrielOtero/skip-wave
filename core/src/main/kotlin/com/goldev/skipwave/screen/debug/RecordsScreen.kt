package com.goldev.skipwave.screen.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.goldev.skipwave.SkipWave
import com.goldev.skipwave.component.LifeComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.ui.model.RecordsModel
import com.goldev.skipwave.ui.view.RecordsView
import com.goldev.skipwave.ui.view.recordsView
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

/**
 *  It's a screen for test RecordsView
 *
 *  @property game The property with game data.
 *  @constructor Creates RecordsScreen
 */
class RecordsScreen(private val game: SkipWave) : KtxScreen {
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
     *  Property with model for the RecordsModel.
     */
    private val model = RecordsModel(eWorld, game.bundle, game.gamePreferences, stage, stage)

    /**
     *  It's a property that contains a RecordsView for the game.
     */
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
     * This function is called when this RecordsScreen appears.
     */
    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            recordView = recordsView(model)
        }
        stage.isDebugAll = true

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
     * It disposes all resources when RecordsScreen is closed.
     */
    override fun dispose() {
        stage.disposeSafely()
    }
}