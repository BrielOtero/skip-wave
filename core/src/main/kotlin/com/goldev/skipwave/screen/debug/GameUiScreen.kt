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

/**
 *  It's a screen for test GameView
 *
 *  @property game The property with game data.
 *  @constructor Creates GameUiScreen
 */
class GameUiScreen(private val game: SkipWave) : KtxScreen {
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
     *  Property with model for the GameModel.
     */
    private val model = GameModel(eWorld, game.bundle, stage, stage)

    /**
     *  It's a property that contains a GameView for the game.
     */
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
     * This function is called when this GameUiScreen appears.
     */
    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            gameView = gameView(model)
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

    /**
     * It disposes all resources when GameUiScreen is closed.
     */
    override fun dispose() {
        stage.disposeSafely()
    }
}