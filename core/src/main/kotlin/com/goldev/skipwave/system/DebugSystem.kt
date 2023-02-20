package com.goldev.skipwave.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.ai.TMP_RECT1
import com.goldev.skipwave.ai.TMP_RECT2
import com.goldev.skipwave.system.AttackSystem.Companion.AABB_RECT
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.assets.disposeSafely
import ktx.graphics.use

/**
 * System that takes care of the debug mode in the game.
 *
 * @property phWorld The physic world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Debug system
 */
class DebugSystem(
    private val phWorld: World,
    @Qualifier("gameStage") private val gameStage: Stage

) : IntervalSystem(enabled = false) {
    /**
     *  The physic renderer.
     */
    private val physicRenderer by lazy { Box2DDebugRenderer() }

    /**
     *  The shape renderer.
     */
    private val shapeRenderer by lazy { ShapeRenderer() }

    /**
     *  GLProfiler enabled.
     */
    private val profiler by lazy { GLProfiler(Gdx.graphics).apply { enable() } }

    /**
     *  Camera from the gameStage.
     */
    private val camera = gameStage.camera


    /**
     *  We're using the ShapeRenderer to draw the AABB_RECT and the two TMP_RECTs
     */
    override fun onTick() {
        gameStage.isDebugAll = true
        Gdx.graphics.setTitle(
            buildString {
                append("FPS:${Gdx.graphics.framesPerSecond},")
                append("DrawCalls:${profiler.drawCalls},")
                append("ContactCount:${phWorld.contactCount},")
                append("FixtureCount:${phWorld.fixtureCount},")
                append("Binds:${profiler.textureBindings},")
                append("Entities:${world.numEntities}")
            }
        )
        physicRenderer.render(phWorld, camera.combined)
        shapeRenderer.use(ShapeRenderer.ShapeType.Line, camera.combined) {
            it.setColor(1f, 0f, 0f, 0f)
            it.rect(
                AABB_RECT.x,
                AABB_RECT.y,
                AABB_RECT.width - AABB_RECT.x,
                AABB_RECT.height - AABB_RECT.y
            )
            it.setColor(1f, 1f, 0f, 0f)
            it.rect(TMP_RECT1.x, TMP_RECT1.y, TMP_RECT1.width, TMP_RECT1.height)
            it.rect(TMP_RECT2.x, TMP_RECT2.y, TMP_RECT2.width, TMP_RECT2.height)
        }
        profiler.reset()
    }

    /**
     * If the debug renderer is enabled, dispose of the physics and shape renderers
     */
    override fun onDispose() {
        if (enabled) {
            physicRenderer.disposeSafely()
            shapeRenderer.disposeSafely()
        }
    }

}