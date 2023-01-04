package com.gabriel.system

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.system.AttackSystem.Companion.AABB_RECT
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.assets.disposeSafely
import ktx.graphics.use

class DebugSystem(
    private val phWorld: World,
    @Qualifier("gameStage") private val gameStage: Stage
) : IntervalSystem(enabled = true) {

    private lateinit var physicRenderer: Box2DDebugRenderer
    private lateinit var shapeRenderer: ShapeRenderer

    init {
        if (enabled) {
            physicRenderer = Box2DDebugRenderer()
            shapeRenderer = ShapeRenderer()
        }
    }

    override fun onTick() {
        physicRenderer.render(phWorld, gameStage.camera.combined)
        shapeRenderer.use(ShapeRenderer.ShapeType.Line, gameStage.camera.combined) {
            it.setColor(1f, 0f, 0f, 0f)
            it.rect(AABB_RECT.x, AABB_RECT.y, AABB_RECT.width - AABB_RECT.x, AABB_RECT.height - AABB_RECT.y)
        }
    }

    override fun onDispose() {
        if (enabled) {
            physicRenderer.disposeSafely()
            shapeRenderer.disposeSafely()
        }
    }

}